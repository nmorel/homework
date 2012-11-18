package com.github.nmorel.homework.api.services.impl;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.xml.ws.http.HTTPException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.nmorel.homework.api.config.CachedGithubRequest;
import com.github.nmorel.homework.api.config.Config;
import com.github.nmorel.homework.api.model.parser.HttpResponseParser;
import com.github.nmorel.homework.api.providers.UserTokenProvider;
import com.github.nmorel.homework.api.services.GithubService;
import com.google.api.client.http.EmptyContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpStatusCodes;
import com.google.api.client.http.HttpTransport;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Inject;

/**
 * Default implementation of {@link GithubService}. It uses an in-memory cache.
 * 
 * @author Nicolas Morel
 */
public class GithubServiceImpl
    implements GithubService
{
    private static final Logger logger = LoggerFactory.getLogger( GithubServiceImpl.class );

    private static final String ACCESS_TOKEN_PARAM = "access_token";

    /**
     * We cache the result of request for a given url. Since we doesn't put data in request body, this is enough. A more
     * complete solution would be to create a key object containing the method, the url and the body data.
     */
    private Cache<String, CachedGithubRequest<?>> cache = CacheBuilder.newBuilder().maximumSize( 50 ).build();

    @Inject
    private UserTokenProvider userTokenProvider;

    @Inject
    private Config config;

    @Inject
    private HttpTransport httpTransport;

    @Override
    @SuppressWarnings( "unchecked" )
    public <T> T execute( String method, GenericUrl url, HttpResponseParser<T> parser, boolean cacheable )
    {
        logger.debug( "Executing request {} {} (cacheable:{})", method, url, cacheable );

        String cacheKey = null;
        CachedGithubRequest<T> cachedResult = null;
        if ( cacheable )
        {
            // we get the url before setting the token so the cache works between multiple users.
            cacheKey = url.build();
            cachedResult = (CachedGithubRequest<T>) cache.getIfPresent( cacheKey );
        }

        // If the token has already been set by the caller, we don't override it
        if ( null == url.get( ACCESS_TOKEN_PARAM ) )
        {
            Optional<String> token = userTokenProvider.get();
            if ( token.isPresent() )
            {
                logger.debug( "Token found => authenticated request" );
                url.set( ACCESS_TOKEN_PARAM, token.get() );
            }
        }
        else
        {
            logger.debug( "Token found => authenticated request" );
        }

        HttpRequest request;
        try
        {
            request = httpTransport.createRequestFactory().buildRequest( method, url, new EmptyContent() );
        }
        catch ( IOException e )
        {
            // no idea how we could fall into this exception, probably when we give a request body.
            logger.error( "Error while writing the request {} {}", method, url, e );
            throw new WebApplicationException( e, HttpStatusCodes.STATUS_CODE_SERVER_ERROR );
        }

        // we handle the status code ourselves
        request.setThrowExceptionOnExecuteError( false );

        if ( null != cachedResult )
        {
            logger.debug( "We already have this request in cache, we set the If-Modified-Since header to {}",
                cachedResult.getLastModified() );
            request.getHeaders().setIfModifiedSince( cachedResult.getLastModified() );
        }

        HttpResponse response;
        try
        {
            response = request.execute();
        }
        catch ( IOException e )
        {
            logger.error( "Error during the execution of the request {} {}", method, url, e );
            return fallbackToCacheOrThrow( cachedResult );
        }

        if ( logger.isDebugEnabled() )
        {
            logger.debug( "The execution of the request succeeded" );
            logger.debug( "Number of requests left : {}/{}", response.getHeaders().get( "X-RateLimit-Remaining" ),
                response.getHeaders().get( "X-RateLimit-Limit" ) );
        }

        if ( response.isSuccessStatusCode() )
        {
            // no disconnect is called on response. The parser must take care of it.
            T result;
            try
            {
                result = parser.parseResponse( response );
                logger.trace( "Result of the request : {}", result );
            }
            catch ( IOException e1 )
            {
                logger.error( "Error while reading the response of the request {} {}", method, url, e1 );
                closeResponse( response );
                return fallbackToCacheOrThrow( cachedResult );
            }
            if ( cacheable )
            {
                String lastModified = response.getHeaders().getLastModified();
                if ( !Strings.isNullOrEmpty( lastModified ) )
                {
                    logger.debug( "Putting the result in cache with the Last-Modified at : {}", lastModified );
                    cache.put( cacheKey, new CachedGithubRequest<T>( lastModified, result ) );
                }
            }
            return result;
        }

        try
        {
            if ( response.getStatusCode() == HttpStatusCodes.STATUS_CODE_NOT_MODIFIED )
            {
                if ( null != cachedResult )
                {
                    logger.debug( "The result has not been modified since last request, we return the cached result" );
                    return cachedResult.getResult();
                }
            }

            // TODO handle error
            throw new WebApplicationException( response.getStatusCode() );
        }
        finally
        {
            closeResponse( response );
        }
    }

    private void closeResponse( HttpResponse response )
    {
        if ( null != response )
        {
            try
            {
                response.disconnect();
            }
            catch ( IOException e )
            {
                logger.warn( "Error disconnecting the response", e );
            }
        }
    }

    private <T> T fallbackToCacheOrThrow( CachedGithubRequest<T> cachedResult )
    {
        // if we have a cache, we fallback to it otherwise we return a 500 error
        if ( null != cachedResult )
        {
            logger.warn( "Falling back to the cached result" );
            return cachedResult.getResult();
        }
        else
        {
            throw new HTTPException( HttpStatusCodes.STATUS_CODE_SERVER_ERROR );
        }
    }

    @Override
    public GenericUrl newGithubUrl()
    {
        return new GenericUrl( config.getGithubApiBaseUrl() );
    }

    @Override
    public Optional<GenericUrl> newAuthenticatedGithubUrl()
    {
        Optional<String> token = userTokenProvider.get();
        if ( token.isPresent() )
        {
            GenericUrl url = newGithubUrl();
            url.set( ACCESS_TOKEN_PARAM, token.get() );
            return Optional.of( url );
        }
        return Optional.absent();
    }
}
