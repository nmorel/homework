package com.github.nmorel.homework.api.repos;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.github.nmorel.homework.api.AbstractApiIT;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.RecordedRequest;

public class RequestCacheIT
    extends AbstractApiIT
{

    @Test
    public void getCommitsTestCache()
        throws IOException, InterruptedException
    {
        String ifModifiedSince = "Tue, 18 Sep 2012 19:32:37 GMT";

        /*
         * we specify the header to add the response to the cache
         */
        githubMock.enqueue( new MockResponse().setResponseCode( 200 )
            .setBody( getMockedBody( "repos/commits/play.json" ) ).setHeader( LAST_MODIFIED, ifModifiedSince ) );

        GenericUrl url = url( "/repos/test/cache/commits" );

        // execute
        HttpResponse response = executeGetRequest( url );

        assertResponseOk( response );
        assertContent( response, "repos/commits/play.result.json" );

        RecordedRequest request = githubMock.takeRequest();
        assertGetUrl( "/repos/test/cache/commits?per_page=100&client_id=1234567890&client_secret=9876543210987654321", request );

        /*
         * in case of an error, the api returns the cached result
         */
        githubMock.enqueue( new MockResponse().setResponseCode( 409 ) );

        // execute
        response = executeGetRequest( url );

        assertResponseOk( response );
        assertContent( response, "repos/commits/play.result.json" );

        request = githubMock.takeRequest();
        assertGetUrl( "/repos/test/cache/commits?per_page=100&client_id=1234567890&client_secret=9876543210987654321", request );
        assertEquals( ifModifiedSince, request.getHeader( IF_MODIFIED_SINCE ) );

        /*
         * in case github returns a 304 not modified, the cache is returned
         */
        githubMock.enqueue( new MockResponse().setResponseCode( 304 ) );

        // execute
        response = executeGetRequest( url );

        assertResponseOk( response );
        assertContent( response, "repos/commits/play.result.json" );

        request = githubMock.takeRequest();
        assertGetUrl( "/repos/test/cache/commits?per_page=100&client_id=1234567890&client_secret=9876543210987654321", request );
        assertEquals( ifModifiedSince, request.getHeader( IF_MODIFIED_SINCE ) );
        
        /*
         * We verify that we don't return the cached result with a 200 OK on the same url
         */
        ifModifiedSince = "Tue, 19 Sep 2012 19:32:37 GMT";
        
        githubMock.enqueue( new MockResponse().setResponseCode( 200 )
            .setBody( getMockedBody( "repos/commits/play2.json" ) ).setHeader( LAST_MODIFIED, ifModifiedSince ) );

        // execute
        response = executeGetRequest( url );
        
        assertResponseOk( response );
        assertContent( response, "repos/commits/play2.result.json" );

        request = githubMock.takeRequest();
        assertGetUrl( "/repos/test/cache/commits?per_page=100&client_id=1234567890&client_secret=9876543210987654321", request );
        
        /*
         * We verify that we return the previous result from cache and not the first one
         */
        githubMock.enqueue( new MockResponse().setResponseCode( 304 ) );

        // execute
        response = executeGetRequest( url );

        assertResponseOk( response );
        assertContent( response, "repos/commits/play2.result.json" );

        request = githubMock.takeRequest();
        assertGetUrl( "/repos/test/cache/commits?per_page=100&client_id=1234567890&client_secret=9876543210987654321", request );
        assertEquals( ifModifiedSince, request.getHeader( IF_MODIFIED_SINCE ) );
    }

}
