package com.github.nmorel.homework.api.repos;

import java.io.IOException;

import org.junit.Test;

import com.github.nmorel.homework.api.AbstractApiIT;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.RecordedRequest;

public class GetCommitsIT
    extends AbstractApiIT
{

    @Test
    public void getCommitsTestEmptyError()
        throws IOException, InterruptedException
    {
        githubMock.enqueue( new MockResponse().setResponseCode( 409 ).setBody(
            getMockedBody( "repos/commits/empty.json" ) ) );

        GenericUrl url = url( "/repos/luchodelavega/toto/commits" );

        // execute
        HttpResponse response = executeGetRequest( url );

        assertResponseOk( response );
        assertContent( response, "repos/commits/empty.result.json" );

        RecordedRequest request = githubMock.takeRequest();
        assertGetUrl( "/repos/luchodelavega/toto/commits?per_page=100&client_id=1234567890&client_secret=9876543210987654321", request );
    }

    @Test
    public void getCommitsTestOk()
        throws IOException, InterruptedException
    {
        githubMock.enqueue( new MockResponse().setResponseCode( 200 ).setBody(
            getMockedBody( "repos/commits/play.json" ) ) );

        GenericUrl url = url( "/repos/playframework/play/commits" );

        // execute
        HttpResponse response = executeGetRequest( url );

        assertResponseOk( response );
        assertContent( response, "repos/commits/play.result.json" );

        RecordedRequest request = githubMock.takeRequest();
        assertGetUrl( "/repos/playframework/play/commits?per_page=100&client_id=1234567890&client_secret=9876543210987654321", request );
    }

}
