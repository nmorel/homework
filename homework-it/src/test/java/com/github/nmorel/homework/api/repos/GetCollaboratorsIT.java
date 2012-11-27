package com.github.nmorel.homework.api.repos;

import java.io.IOException;

import org.junit.Test;

import com.github.nmorel.homework.api.AbstractApiIT;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.RecordedRequest;

public class GetCollaboratorsIT
    extends AbstractApiIT
{
    @Test
    public void getCollaboratorsTestOk()
        throws IOException, InterruptedException
    {
        githubMock.enqueue( new MockResponse().setResponseCode( 200 ).setBody(
            getMockedBody( "repos/collaborators/play.json" ) ) );

        GenericUrl url = url( "/repos/playframework/play/collaborators" );

        // execute
        HttpResponse response = executeGetRequest( url );

        assertResponseOk( response );
        assertContent( response, "repos/collaborators/play.result.json" );

        RecordedRequest request = githubMock.takeRequest();
        assertGetUrl( "/repos/playframework/play/collaborators", request );
    }

}
