package com.github.nmorel.homework.api.parsers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.github.nmorel.homework.api.model.User;
import com.google.api.client.http.HttpResponse;
import com.google.gson.reflect.TypeToken;

@RunWith( PowerMockRunner.class )
@PrepareForTest( HttpResponse.class )
public class GsonHttpResponseParserTest
{
    @Test
    public void parseResponseWithClass()
        throws IOException
    {
        GsonHttpResponseParser<User[]> parser = new GsonHttpResponseParser<>( User[].class );

        try (InputStream is = getClass().getResourceAsStream( "collaborators.json" ))
        {
            HttpResponse response = PowerMockito.mock( HttpResponse.class );
            when( response.getContent() ).thenReturn( is );

            User[] result = parser.parseResponse( response );
            assertEquals( "3 collaborators is expected", 3, result.length );
            assertFirstCollaborator( result[0] );
            assertSecondCollaborator( result[1] );
            assertThirdCollaborator( result[2] );

            verify( response, times( 1 ) ).disconnect();
        }
    }

    @Test
    public void parseResponseWithType()
        throws IOException
    {
        Type type = new TypeToken<List<User>>() {}.getType();
        GsonHttpResponseParser<List<User>> parser = new GsonHttpResponseParser<>( type );

        try (InputStream is = getClass().getResourceAsStream( "collaborators.json" ))
        {
            HttpResponse response = PowerMockito.mock( HttpResponse.class );
            when( response.getContent() ).thenReturn( is );

            List<User> result = parser.parseResponse( response );
            assertEquals( "3 collaborators is expected", 3, result.size() );
            assertFirstCollaborator( result.get( 0 ) );
            assertSecondCollaborator( result.get( 1 ) );
            assertThirdCollaborator( result.get( 2 ) );

            verify( response, times( 1 ) ).disconnect();
        }
    }

    /**
     * Test that we always call the disconnect method on response even in case of an exception
     * 
     * @throws IOException
     */
    @Test
    public void parseResponseWithException()
        throws IOException
    {
        GsonHttpResponseParser<User[]> parser = new GsonHttpResponseParser<>( User[].class );

        HttpResponse response = PowerMockito.mock( HttpResponse.class );
        when( response.getContent() ).thenThrow( new IOException() );

        try
        {
            parser.parseResponse( response );
            fail();
        }
        catch ( IOException e )
        {
            // expected exception
        }

        verify( response, times( 1 ) ).disconnect();
    }

    private void assertFirstCollaborator( User collaborator )
    {
        assertEquals(
            "https://secure.gravatar.com/avatar/12fda944ce03f21eb4f7f9fdd7512a8e?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png",
            collaborator.getAvatarUrl() );
        assertEquals( "Warry", collaborator.getLogin() );
        // this request doesn't give email and name informations
        assertNull( "email should be null", collaborator.getEmail() );
        assertNull( "name should be null", collaborator.getName() );
    }

    private void assertSecondCollaborator( User collaborator )
    {
        assertEquals(
            "https://secure.gravatar.com/avatar/df2d6f1d39a958e46c75e61d4ebb43af?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png",
            collaborator.getAvatarUrl() );
        assertEquals( "erwan", collaborator.getLogin() );
        // this request doesn't give email and name informations
        assertNull( "email should be null", collaborator.getEmail() );
        assertNull( "name should be null", collaborator.getName() );
    }

    private void assertThirdCollaborator( User collaborator )
    {
        assertEquals(
            "https://secure.gravatar.com/avatar/adcd749d588278dbd255068c1d4b20d3?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png",
            collaborator.getAvatarUrl() );
        assertEquals( "guillaumebort", collaborator.getLogin() );
        // this request doesn't give email and name informations
        assertNull( "email should be null", collaborator.getEmail() );
        assertNull( "name should be null", collaborator.getName() );
    }
}
