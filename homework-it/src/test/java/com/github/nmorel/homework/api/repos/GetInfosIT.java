package com.github.nmorel.homework.api.repos;

import java.io.IOException;

import org.junit.Test;

import com.github.nmorel.homework.api.AbstractApiIT;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.RecordedRequest;

public class GetInfosIT
    extends AbstractApiIT
{
    @Test
    public void getInfosTestOk()
        throws IOException, InterruptedException
    {
        githubMock.enqueue( new MockResponse().setResponseCode( 200 ).setBody(
            getMockedBody( "repos/infos/play.json" ) ) );

        GenericUrl url = url( "/repos/playframework/play" );

        // execute
        HttpResponse response = executeGetRequest( url );

        assertResponseOk( response );
        assertContent( response, "repos/infos/play.result.json" );

        RecordedRequest request = githubMock.takeRequest();
        assertGetUrl( "/repos/playframework/play?client_id=1234567890&client_secret=9876543210987654321", request );
    }

}
