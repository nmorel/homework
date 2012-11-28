package com.github.nmorel.homework.api.repos;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.github.nmorel.homework.api.AbstractApiIT;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.RecordedRequest;

public class SearchIT
    extends AbstractApiIT
{

    @Test
    public void searchTestEmptyResult()
        throws IOException, InterruptedException
    {
        String expectedResult = getMockedBody( "repos/search/empty.json" );
        githubMock.enqueue( new MockResponse().setResponseCode( 200 ).setBody( expectedResult ) );

        GenericUrl url = url( "/repos/search" );
        url.set( "keyword", "totoazsdfdfg" );

        // execute
        HttpResponse response = executeGetRequest( url );

        assertResponseOk( response );

        String actual = response.parseAsString();
        assertEquals( "the api should returns the exact same result", expectedResult, actual );

        RecordedRequest request = githubMock.takeRequest();
        assertGetUrl( "/legacy/repos/search/totoazsdfdfg?client_id=1234567890&client_secret=9876543210987654321", request );
    }
    
    @Test
    public void searchTestResultOk()
        throws IOException, InterruptedException
    {
        String expectedResult = getMockedBody( "repos/search/scala.json" );
        githubMock.enqueue( new MockResponse().setResponseCode( 200 ).setBody( expectedResult ) );

        GenericUrl url = url( "/repos/search" );
        url.set( "keyword", "scala" );

        // execute
        HttpResponse response = executeGetRequest( url );

        assertResponseOk( response );

        String actual = response.parseAsString();
        assertEquals( "the api should returns the exact same result", expectedResult, actual );

        RecordedRequest request = githubMock.takeRequest();
        assertGetUrl( "/legacy/repos/search/scala?client_id=1234567890&client_secret=9876543210987654321", request );
        assertNull( request.getHeader( "If-Modified-Since" ) );
    }
    
    @Test
    public void searchTestError()
        throws IOException, InterruptedException
    {
        githubMock.enqueue( new MockResponse().setResponseCode( 409 ) );

        GenericUrl url = url( "/repos/search" );
        url.set( "keyword", "scala" );

        // execute
        HttpResponse response = executeGetRequest( url );

        assertResponseKo( response, 409 );

        RecordedRequest request = githubMock.takeRequest();
        assertGetUrl( "/legacy/repos/search/scala?client_id=1234567890&client_secret=9876543210987654321", request );
    }

}
