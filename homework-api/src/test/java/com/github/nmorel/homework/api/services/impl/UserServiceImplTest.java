package com.github.nmorel.homework.api.services.impl;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.nmorel.homework.api.model.User;
import com.github.nmorel.homework.api.parsers.GsonHttpResponseParser;
import com.github.nmorel.homework.api.parsers.HttpResponseParser;
import com.github.nmorel.homework.api.services.GithubService;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpMethods;
import com.google.common.base.Optional;

@RunWith( MockitoJUnitRunner.class )
public class UserServiceImplTest
{
    @Mock
    private GithubService githubService;

    private UserServiceImpl service;

    @Before
    public void init()
    {
        service = new UserServiceImpl( githubService );
    }

    /**
     * If the current user isn't authenticated, no exception is thrown and the service returns an empty result.
     */
    @SuppressWarnings( "unchecked" )
    @Test
    public void getAuthenticatedUserInformationsTestNotAuthenticated()
    {
        when( githubService.newAuthenticatedGithubUrl() ).thenReturn( Optional.<GenericUrl> absent() );

        Optional<User> user = service.getAuthenticatedUserInformations();
        assertFalse( user.isPresent() );

        verify( githubService, never() ).execute( any( String.class ), any( GenericUrl.class ),
            any( HttpResponseParser.class ), any( boolean.class ) );
    }

    /**
     * If the token is revoked, the githubService will throw an exception. The service must not throw an exception and
     * just returns an empty result.
     */
    @SuppressWarnings( "unchecked" )
    @Test
    public void getAuthenticatedUserInformationsTestTokenRevoked()
    {
        GenericUrl url = new GenericUrl( "http://api.github.test.com" );
        url.set( "access_token", "1234567890" );
        when( githubService.newAuthenticatedGithubUrl() ).thenReturn( Optional.of( url ) );
        when( githubService.execute( eq( HttpMethods.GET ), eq( url ), any( GsonHttpResponseParser.class ), eq( true ) ) )
            .thenThrow( new WebApplicationException( Status.UNAUTHORIZED ) );

        Optional<User> user = service.getAuthenticatedUserInformations();
        assertFalse( user.isPresent() );
    }

    /**
     * When the current user is authenticated and the request returns with no error. The service must not modify the
     * returned user.
     */
    @SuppressWarnings( "unchecked" )
    @Test
    public void getAuthenticatedUserInformationsTestOk()
    {
        GenericUrl url = new GenericUrl( "http://api.github.test.com" );
        url.set( "access_token", "1234567890" );
        when( githubService.newAuthenticatedGithubUrl() ).thenReturn( Optional.of( url ) );

        User expectedUser = new User();
        expectedUser.setLogin( "myLogin" );
        expectedUser.setAvatarUrl( "myAvatarUrl" );
        when( githubService.execute( eq( HttpMethods.GET ), eq( url ), any( GsonHttpResponseParser.class ), eq( true ) ) )
            .thenReturn( expectedUser );

        Optional<User> actualUser = service.getAuthenticatedUserInformations();
        assertTrue( actualUser.isPresent() );
        assertSame( expectedUser, actualUser.get() );
        assertEquals( "myLogin", actualUser.get().getLogin() );
        assertEquals( "myAvatarUrl", actualUser.get().getAvatarUrl() );
        assertNull( actualUser.get().getName() );
        assertNull( actualUser.get().getEmail() );
    }
}
