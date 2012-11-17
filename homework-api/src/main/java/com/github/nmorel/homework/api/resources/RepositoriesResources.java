package com.github.nmorel.homework.api.resources;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.nmorel.homework.api.config.Config;
import com.github.nmorel.homework.api.config.UserId;
import com.github.nmorel.homework.api.services.OAuthTokenService;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;

@Path( "repos" )
public class RepositoriesResources
{
    private static final Logger logger = LoggerFactory.getLogger( RepositoriesResources.class );

    @Inject
    private HttpTransport httpTransport;

    @Inject
    private Config config;

    @Inject
    private Provider<HttpServletRequest> httpServletRequest;

    @Inject
    private OAuthTokenService tokenService;

    @Inject
    @UserId
    private Provider<String> userId;

    @GET
    public StreamingOutput search( @QueryParam( "query" ) String query )
        throws IOException
    {
        Optional<String> token = tokenService.getToken( userId.get() );

        logger.info( "Looking for repositories with the keyword '{}'", query );

        GenericUrl url = new GenericUrl( config.getGithubApiBaseUrl() + "/legacy/repos/search/" + query );
        if ( token.isPresent() )
        {
            url.set( "access_token", token.get() );
        }

        final HttpResponse response = httpTransport.createRequestFactory().buildGetRequest( url ).execute();

        logger.info( "limit : {}", response.getHeaders().get( "X-RateLimit-Remaining" ) );

        return new StreamingOutput() {

            @Override
            public void write( OutputStream output )
                throws IOException, WebApplicationException
            {
                response.download( output );
            }
        };
    }

}
