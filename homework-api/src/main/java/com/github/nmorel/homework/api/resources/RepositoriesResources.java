package com.github.nmorel.homework.api.resources;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

@Path( "repo" )
public class RepositoriesResources
{
    private static final Logger logger = LoggerFactory.getLogger( RepositoriesResources.class );

    @Inject
    private Provider<HttpClient> client;

    @Inject
    private Provider<HttpServletRequest> httpRequest;

    @GET
    public StreamingOutput search( @QueryParam( "query" ) String query )
        throws ClientProtocolException, IOException
    {
        logger.info( "Looking for repositories with the keyword '{}'", query );
        HttpGet request = new HttpGet( "https://api.github.com/legacy/repos/search/" + query );
        final HttpResponse response = client.get().execute( request );
        return new StreamingOutput() {

            @Override
            public void write( OutputStream output )
                throws IOException, WebApplicationException
            {
                response.getEntity().writeTo( output );
            }
        };
    }

}
