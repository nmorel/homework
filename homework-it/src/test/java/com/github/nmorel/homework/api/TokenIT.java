package com.github.nmorel.homework.api;

import java.io.IOException;

import org.junit.Test;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.RecordedRequest;

public class TokenIT
    extends AbstractApiIT
{
    /**
     * We test that the token given to the api is correctly passed to github
     * 
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void getInfosTestOk()
        throws IOException, InterruptedException
    {
        githubMock.enqueue( new MockResponse().setResponseCode( 200 ).setBody(
            getMockedBody( "repos/search/empty.json" ) ) );

        GenericUrl url = url( "/repos/search" );
        url.set( "keyword", "totoazsdfdfg" );
        url.set( "access_token", "12345678901234567890" );

        // execute
        HttpResponse response = executeGetRequest( url );

        assertResponseOk( response );

        RecordedRequest request = githubMock.takeRequest();
        assertGetUrl( "/legacy/repos/search/totoazsdfdfg?access_token=12345678901234567890", request );
    }
}
