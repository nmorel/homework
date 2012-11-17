package com.github.nmorel.homework.api.resources;

import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.nmorel.homework.api.config.Config;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.inject.Inject;

@Path( "repo" )
public class RepositoriesResources
{
    private static final Logger logger = LoggerFactory.getLogger( RepositoriesResources.class );

    @Inject
    private HttpTransport httpTransport;

    @Inject
    private Config config;

    @GET
    public StreamingOutput search( @QueryParam( "query" ) String query )
        throws IOException
    {
        logger.info( "Looking for repositories with the keyword '{}'", query );
        final HttpResponse response =
            httpTransport.createRequestFactory()
                .buildGetRequest( new GenericUrl( config.getGithubApiBaseUrl() + "/legacy/repos/search/" + query ) )
                .execute();
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
