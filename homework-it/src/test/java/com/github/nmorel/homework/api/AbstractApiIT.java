package com.github.nmorel.homework.api;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;

import com.google.api.client.http.EmptyContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.mockwebserver.MockWebServer;
import com.google.mockwebserver.RecordedRequest;

/**
 * Abstract class used to manage the github server mock and give a few utility method
 * 
 * @author Nicolas Morel
 */
public abstract class AbstractApiIT
{
    protected static final String URL_BASE = "http://localhost:8090";
    protected static final String LAST_MODIFIED = "Last-Modified";
    protected static final String IF_MODIFIED_SINCE = "If-Modified-Since";
    protected static final HttpTransport httpTransport = new NetHttpTransport();
    protected MockWebServer githubMock;

    @Before
    public void startGithubMock()
        throws IOException
    {
        githubMock = new MockWebServer();
        githubMock.play( 8091 );
    }

    @After
    public void shutdownGithubMock()
        throws IOException
    {
        githubMock.shutdown();
    }

    protected GenericUrl url()
    {
        return new GenericUrl( URL_BASE + "/api" );
    }

    protected GenericUrl url( String path )
    {
        GenericUrl url = url();
        if ( !Strings.isNullOrEmpty( path ) )
        {
            url.appendRawPath( path );
        }
        return url;
    }

    protected HttpRequest buildGetRequest( GenericUrl url )
        throws IOException
    {
        HttpRequest request = httpTransport.createRequestFactory().buildGetRequest( url );
        request.setThrowExceptionOnExecuteError( false );
        return request;
    }

    protected HttpResponse executeGetRequest( GenericUrl url )
        throws IOException
    {
        return buildGetRequest( url ).execute();
    }

    protected HttpRequest buildPostRequest( GenericUrl url )
        throws IOException
    {
        return httpTransport.createRequestFactory().buildPostRequest( url, new EmptyContent() );
    }

    protected HttpResponse executePostRequest( GenericUrl url )
        throws IOException
    {
        return buildPostRequest( url ).execute();
    }

    protected String getMockedBody( String file )
        throws IOException
    {
        return Files.toString( getTestFile( file ), Charsets.UTF_8 );
    }

    protected File getTestFile( String file )
    {
        return new File( "src/test/resources", file );
    }

    protected void assertResponseOk( HttpResponse response )
    {
        assertEquals( 200, response.getStatusCode() );
        assertEquals( "application/json", response.getContentType() );
    }

    protected void assertResponseKo( HttpResponse response, int status )
    {
        assertEquals( status, response.getStatusCode() );
    }

    protected void assertUrl( String method, String path, RecordedRequest request )
    {
        assertEquals( method + " " + path + " HTTP/1.1", request.getRequestLine() );
    }

    protected void assertGetUrl( String path, RecordedRequest request )
    {
        assertUrl( "GET", path, request );
    }

    protected void assertContent( HttpResponse response, String expectedFile )
        throws JsonSyntaxException, JsonIOException, IOException
    {
        // parsing the expected result with gson to remove the pretty style
        Gson gson = new Gson();
        JsonElement element = gson.fromJson( new FileReader( getTestFile( expectedFile ) ), JsonElement.class );
        assertEquals( "The content of the response isn't equal to the expected result", gson.toJson( element ),
            response.parseAsString() );
    }
}
