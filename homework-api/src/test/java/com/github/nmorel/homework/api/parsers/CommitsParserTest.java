package com.github.nmorel.homework.api.parsers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.github.nmorel.homework.api.model.Commit;
import com.google.api.client.http.HttpResponse;

@RunWith( PowerMockRunner.class )
@PrepareForTest( HttpResponse.class )
public class CommitsParserTest
{
    /**
     * Test that we always call the disconnect method on response even in case of an exception
     * 
     * @throws IOException
     */
    @Test
    public void parserResponseTestWithException()
        throws IOException
    {
        CommitsParser parser = new CommitsParser();

        HttpResponse response = PowerMockito.mock( HttpResponse.class );
        when( response.getContent() ).thenThrow( new IOException() );

        try
        {
            parser.parseResponse( response );
            fail();
        }
        catch ( IOException e )
        {
        }

        verify( response, times( 1 ) ).disconnect();
    }

    /**
     * Test the parser with correct data. We test the sorting by author date and when the committer and author are null.
     * 
     * @throws IOException
     */
    @Test
    public void parseResponseTestOk()
        throws IOException
    {
        CommitsParser parser = new CommitsParser();

        try (InputStream is = getClass().getResourceAsStream( "commits.json" ))
        {
            HttpResponse response = PowerMockito.mock( HttpResponse.class );
            when( response.getContent() ).thenReturn( is );

            List<Commit> result = parser.parseResponse( response );
            assertEquals( "3 commits is expected", 3, result.size() );
            assertFirstCommit( result.get( 0 ) );
            assertSecondCommit( result.get( 1 ) );
            assertThirdCommit( result.get( 2 ) );

            verify( response, times( 1 ) ).disconnect();
        }
    }

    private void assertFirstCommit( Commit commit )
    {
        assertEquals( "8492a0b904605bfad6110fe2efaadebea9ce6497", commit.getSha() );
        assertEquals( "[#1309] Include explicit query paramters that are also in routeArgs", commit.getMessage() );
        assertEquals(
            "https://secure.gravatar.com/avatar/c2b2e8ba6cda232e02e1f337f7b9ae52?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png",
            commit.getAuthor().getAvatarUrl() );
        assertEquals( "2011-12-12T18:31:45Z", commit.getAuthor().getDate() );
        assertEquals( "colesbury@gmail.com", commit.getAuthor().getEmail() );
        assertEquals( "colesbury", commit.getAuthor().getLogin() );
        assertEquals( "Sam Gross", commit.getAuthor().getName() );
        assertEquals(
            "https://secure.gravatar.com/avatar/c6ca9f2d6facabf69800e3e37c7e06d1?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png",
            commit.getCommitter().getAvatarUrl() );
        assertEquals( "2012-03-14T10:23:42Z", commit.getCommitter().getDate() );
        assertEquals( "mbk@kjetland.com", commit.getCommitter().getEmail() );
        assertEquals( "mbknor", commit.getCommitter().getLogin() );
        assertEquals( "Morten Kjetland", commit.getCommitter().getName() );
    }

    private void assertSecondCommit( Commit commit )
    {
        assertEquals( "c949cfce4c693e8b1a7b69721d7098993213e925", commit.getSha() );
        assertEquals( "should not compare locale string to language string", commit.getMessage() );
        assertNull( commit.getAuthor().getAvatarUrl() );
        assertEquals( "2012-05-10T13:44:01Z", commit.getAuthor().getDate() );
        assertEquals( "neeme@apache.org", commit.getAuthor().getEmail() );
        assertNull( commit.getAuthor().getLogin() );
        assertEquals( "Neeme Praks", commit.getAuthor().getName() );
        assertNull( commit.getCommitter().getAvatarUrl() );
        assertEquals( "2012-05-10T13:44:01Z", commit.getCommitter().getDate() );
        assertEquals( "neeme@apache.org", commit.getCommitter().getEmail() );
        assertNull( commit.getCommitter().getLogin() );
        assertEquals( "Neeme Praks", commit.getCommitter().getName() );
    }

    private void assertThirdCommit( Commit commit )
    {
        assertEquals( "9c5bf9f48fe18a07008819229ce59c0238893511", commit.getSha() );
        assertEquals( "[#1162] Session is lost when application.session.maxAge is set to 30d", commit.getMessage() );
        assertEquals(
            "https://secure.gravatar.com/avatar/34fe9578c98748263731073b5013cb07?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png",
            commit.getAuthor().getAvatarUrl() );
        assertEquals( "2012-05-10T14:21:09Z", commit.getAuthor().getDate() );
        assertEquals( "leroux.nicolas@gmail.com", commit.getAuthor().getEmail() );
        assertEquals( "pepite", commit.getAuthor().getLogin() );
        assertEquals( "Nicolas Leroux", commit.getAuthor().getName() );
        assertEquals(
            "https://secure.gravatar.com/avatar/34fe9578c98748263731073b5013cb07?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png",
            commit.getCommitter().getAvatarUrl() );
        assertEquals( "2012-05-10T14:21:27Z", commit.getCommitter().getDate() );
        assertEquals( "leroux.nicolas@gmail.com", commit.getCommitter().getEmail() );
        assertEquals( "pepite", commit.getCommitter().getLogin() );
        assertEquals( "Nicolas Leroux", commit.getCommitter().getName() );
    }
}
