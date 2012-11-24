package com.github.nmorel.homework.api.services.impl;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.github.nmorel.homework.api.config.Config;
import com.github.nmorel.homework.api.config.providers.UserIdProvider;
import com.github.nmorel.homework.api.services.OAuthTokenService;
import com.google.api.client.http.EmptyContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.common.base.Optional;

@RunWith( PowerMockRunner.class )
@PrepareForTest( { HttpTransport.class, HttpRequestFactory.class, HttpRequest.class, HttpResponse.class } )
public class OAuthTokenServiceImplTest
{
    @Mock
    private HttpTransport httpTransport;

    @Mock
    private HttpRequestFactory httpRequestFactory;

    @Mock
    private HttpRequest httpRequest;

    @Mock
    private HttpHeaders httpHeaders;

    @Mock
    private HttpResponse httpResponse;

    @Mock
    private Config config;

    @Mock
    private UserIdProvider userIdProvider;

    private OAuthTokenService service;

    private String url = "https://api.github.test";
    private String clientId = "clientId15447";
    private String clientSecret = "clientSecret4862686754";

    @Before
    public void init()
        throws IOException
    {
        service = new OAuthTokenServiceImpl( userIdProvider, config, httpTransport );

        when( config.getGithubTokenUrl() ).thenReturn( url );
        when( config.getGithubClientId() ).thenReturn( clientId );
        when( config.getGithubClientSecret() ).thenReturn( clientSecret );

        when( httpTransport.createRequestFactory() ).thenReturn( httpRequestFactory );
        when( httpRequestFactory.buildPostRequest( any( GenericUrl.class ), any( EmptyContent.class ) ) ).thenReturn(
            httpRequest );
        when( httpRequest.getHeaders() ).thenReturn( httpHeaders );
    }

    @Test
    public void retrieveAndStoreTokenTestWithCodeNull()
    {
        try
        {
            // execute
            service.retrieveAndStoreToken( null );
            fail();
        }
        catch ( IllegalArgumentException e )
        {
        }

        try
        {
            // execute
            service.retrieveAndStoreToken( "" );
            fail();
        }
        catch ( IllegalArgumentException e )
        {
        }

        // we check that no request to the api is made
        verify( httpTransport, never() ).createRequestFactory();
    }

    /**
     * If no user is present, the method should not call the github api since we can't associate the token to an user
     */
    @Test
    public void retrieveAndStoreTokenTestWithNoUserId()
    {
        when( userIdProvider.get() ).thenReturn( Optional.<String> absent() );

        // execute
        service.retrieveAndStoreToken( "s4fg8d7g8" );

        // we check that no request to the api is made
        verify( httpTransport, never() ).createRequestFactory();
    }

    @Test
    public void retrieveAndStoreTokenTestWithIOException()
        throws IOException
    {
        String userId = "123456";

        when( userIdProvider.get() ).thenReturn( Optional.of( userId ) );

        // on execute, we throw an exception
        when( httpRequest.execute() ).thenThrow( new IOException() );

        // execute
        service.retrieveAndStoreToken( "s4fg8d7g8" );

        // currently, no error is returned after an exception, we just test that no token has been put into cache
        assertFalse( "no token should be returned", service.getToken( userId ).isPresent() );

        // we test the correctness of the url
        ArgumentCaptor<GenericUrl> urlCaptor = ArgumentCaptor.forClass( GenericUrl.class );
        verify( httpRequestFactory, times( 1 ) ).buildPostRequest( urlCaptor.capture(), any( EmptyContent.class ) );
        assertEquals( url + "?client_id=" + clientId + "&client_secret=" + clientSecret + "&code=s4fg8d7g8", urlCaptor
            .getValue().build() );

        // we test that the correct accept has been set
        verify( httpHeaders ).setAccept( "application/json" );
    }

    @Test
    public void retrieveAndStoreTokenTestOk()
        throws IOException
    {
        retrieveAndStoreTokenTest( "123456", "token1.json", "9876543210" );

        // the token should be stored
        assertEquals( "9876543210", service.getToken( "123456" ).get() );

        // we test the correctness of the url
        ArgumentCaptor<GenericUrl> urlCaptor = ArgumentCaptor.forClass( GenericUrl.class );
        verify( httpRequestFactory, times( 1 ) ).buildPostRequest( urlCaptor.capture(), any( EmptyContent.class ) );
        assertEquals( url + "?client_id=" + clientId + "&client_secret=" + clientSecret + "&code=s4fg8d7g8", urlCaptor
            .getValue().build() );

        // we test that the correct accept has been set
        verify( httpHeaders ).setAccept( "application/json" );
    }

    @Test
    public void getTokenAndDeleteTest()
        throws IOException
    {
        // we first test that no exception is thrown when we give incorrect values
        assertFalse( service.getToken( null ).isPresent() );
        assertFalse( service.getToken( "" ).isPresent() );

        // we then test that no exception is thrown when no token is associated to the user
        assertFalse( service.getToken( "123456" ).isPresent() );

        when( userIdProvider.get() ).thenReturn( Optional.<String> absent() );
        assertFalse( service.getToken().isPresent() );
        reset( userIdProvider );

        when( userIdProvider.get() ).thenReturn( Optional.of( "123456" ) );
        assertFalse( service.getToken().isPresent() );
        reset( userIdProvider );

        // we insert tokens
        retrieveAndStoreTokenTest( "123456", "token1.json", "9876543210" );
        retrieveAndStoreTokenTest( "875185", "token2.json", "0123456789" );

        assertEquals( "9876543210", service.getToken( "123456" ).get() );
        assertEquals( "0123456789", service.getToken( "875185" ).get() );

        when( userIdProvider.get() ).thenReturn( Optional.of( "123456" ) );
        assertEquals( "9876543210", service.getToken().get() );
        reset( userIdProvider );

        when( userIdProvider.get() ).thenReturn( Optional.of( "875185" ) );
        assertEquals( "0123456789", service.getToken().get() );
        reset( userIdProvider );

        // we test the deletion now
        service.deleteToken( "123456" );
        assertFalse( service.getToken( "123456" ).isPresent() );
        assertEquals( "0123456789", service.getToken( "875185" ).get() );

        when( userIdProvider.get() ).thenReturn( Optional.of( "875185" ) );
        service.deleteToken();
        assertFalse( service.getToken( "123456" ).isPresent() );
        assertFalse( service.getToken( "875185" ).isPresent() );
    }

    private void retrieveAndStoreTokenTest( String userId, String jsonFile, String token )
        throws IOException
    {
        reset( userIdProvider );

        when( userIdProvider.get() ).thenReturn( Optional.of( userId ) );

        // on execute, we throw an exception
        when( httpRequest.execute() ).thenReturn( httpResponse );
        when( httpResponse.getContent() ).thenReturn( getClass().getResourceAsStream( jsonFile ) );

        // execute
        service.retrieveAndStoreToken( "s4fg8d7g8" );

        reset( userIdProvider );
    }

}
