package com.github.nmorel.homework.api;

import java.io.IOException;

import org.junit.Test;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.MockWebServer;

public class RepositoriesResourcesIT
{
    private static HttpTransport httpTransport = new NetHttpTransport();

    private GenericUrl url()
    {
        return new GenericUrl( "http://localhost:8090/api" );
    }

    @Test
    public void te()
        throws IOException
    {
        MockWebServer githubMock = new MockWebServer();
        githubMock.play( 8091 );
        

        githubMock.enqueue( new MockResponse().setResponseCode( 200 ).setBody( "{\"repositories\":[]}" ) );

        GenericUrl url = url();
        url.appendRawPath( "/repos/search" );
        url.set( "keyword", "toto" );
        HttpResponse response = httpTransport.createRequestFactory().buildGetRequest( url ).execute();
        System.out.println( response.parseAsString() );
        
        githubMock.shutdown();
    }
}
