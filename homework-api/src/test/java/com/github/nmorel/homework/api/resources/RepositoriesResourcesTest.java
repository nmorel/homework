package com.github.nmorel.homework.api.resources;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.nmorel.homework.api.model.Commit;
import com.github.nmorel.homework.api.model.User;
import com.github.nmorel.homework.api.parsers.CommitsParser;
import com.github.nmorel.homework.api.parsers.GsonHttpResponseParser;
import com.github.nmorel.homework.api.parsers.StreamingHttpResponseParser;
import com.github.nmorel.homework.api.services.GithubService;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpMethods;

@RunWith( MockitoJUnitRunner.class )
public class RepositoriesResourcesTest
{

    @Mock
    private GithubService githubService;

    private RepositoriesResources resources;

    @Before
    public void init()
    {
        resources = new RepositoriesResources( githubService );
    }

    /**
     * Test the behaviour of the repository search with empty keyword
     */
    @Test
    public void searchTestWithoutKeyword()
    {
        try
        {
            resources.search( null );
            fail( "Empty keyword isn't allowed" );
        }
        catch ( WebApplicationException e )
        {
            assertEquals( "400 expected", Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus() );
        }

        try
        {
            resources.search( "" );
            fail( "Empty keyword isn't allowed" );
        }
        catch ( WebApplicationException e )
        {
            assertEquals( "400 expected", Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus() );
        }
    }

    /**
     * Test that the search method build the correct url
     */
    @Test
    public void searchTestWithKeyword()
    {
        GenericUrl baseTestUrl = new GenericUrl( "http://api.github.test.com" );
        when( githubService.newGithubUrl() ).thenReturn( baseTestUrl );

        StreamingOutput expectedResult = mock( StreamingOutput.class );
        when(
            githubService.execute( eq( HttpMethods.GET ), same( baseTestUrl ),
                any( StreamingHttpResponseParser.class ), eq( false ) ) ).thenReturn( expectedResult );

        // search
        StreamingOutput actualResult = resources.search( "toto" );

        // the result must be the same as the one returned by the service
        assertSame( "The method shouldn't change the result", expectedResult, actualResult );

        // we capture the url to test it
        ArgumentCaptor<GenericUrl> argument = ArgumentCaptor.forClass( GenericUrl.class );
        verify( githubService ).execute( eq( HttpMethods.GET ), argument.capture(),
            any( StreamingHttpResponseParser.class ), eq( false ) );
        assertEquals( "http://api.github.test.com/legacy/repos/search/toto", argument.getValue().build() );
    }

    /**
     * Test the recovery of the collaborator list
     */
    @SuppressWarnings( "unchecked" )
    @Test
    public void listCollaboratorsTest()
    {
        GenericUrl baseTestUrl = new GenericUrl( "http://api.github.test.com" );
        when( githubService.newGithubUrl() ).thenReturn( baseTestUrl );

        User[] expectedResult = new User[] { new User(), new User() };
        when(
            githubService.execute( eq( HttpMethods.GET ), same( baseTestUrl ), any( GsonHttpResponseParser.class ),
                eq( true ) ) ).thenReturn( expectedResult );

        // list commits
        User[] actualResult = resources.listCollaborators( "toto", "zen" );

        // the result must be the same as the one returned by the service
        assertSame( "The method shouldn't change the result", expectedResult, actualResult );

        // we capture the url to test it
        ArgumentCaptor<GenericUrl> argument = ArgumentCaptor.forClass( GenericUrl.class );
        verify( githubService ).execute( eq( HttpMethods.GET ), argument.capture(),
            any( GsonHttpResponseParser.class ), eq( true ) );
        assertEquals( "http://api.github.test.com/repos/toto/zen/collaborators", argument.getValue().build() );
    }

    /**
     * Test the recovery of the commit list
     */
    @Test
    public void listCommitsTest()
    {
        GenericUrl baseTestUrl = new GenericUrl( "http://api.github.test.com" );
        when( githubService.newGithubUrl() ).thenReturn( baseTestUrl );

        List<Commit> expectedResult = Arrays.asList( new Commit(), new Commit() );
        when(
            githubService.execute( eq( HttpMethods.GET ), same( baseTestUrl ), any( CommitsParser.class ), eq( true ) ) )
            .thenReturn( expectedResult );

        // list commits
        List<Commit> actualResult = resources.listCommits( "toto", "zen" );

        // the result must be the same as the one returned by the service
        assertSame( "The method shouldn't change the result", expectedResult, actualResult );

        // we capture the url to test it
        ArgumentCaptor<GenericUrl> argument = ArgumentCaptor.forClass( GenericUrl.class );
        verify( githubService ).execute( eq( HttpMethods.GET ), argument.capture(), any( CommitsParser.class ),
            eq( true ) );
        assertEquals( "http://api.github.test.com/repos/toto/zen/commits?per_page=100", argument.getValue().build() );
    }
}
