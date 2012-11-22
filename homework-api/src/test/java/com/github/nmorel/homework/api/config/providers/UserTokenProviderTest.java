package com.github.nmorel.homework.api.config.providers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.nmorel.homework.api.config.providers.UserTokenProvider;
import com.github.nmorel.homework.api.services.OAuthTokenService;
import com.google.common.base.Optional;
import com.google.inject.Provider;

@RunWith( MockitoJUnitRunner.class )
public class UserTokenProviderTest
{
    @Mock
    private OAuthTokenService tokenService;

    @Mock
    private HttpServletRequest request;

    private UserTokenProvider userTokenProvider;

    @Before
    public void init()
    {
        userTokenProvider = new UserTokenProvider( new Provider<HttpServletRequest>() {

            @Override
            public HttpServletRequest get()
            {
                return request;
            }
        }, tokenService );
    }

    @Test
    public void testNullRequest()
    {
        UserTokenProvider userTokenProvider = new UserTokenProvider( new Provider<HttpServletRequest>() {

            @Override
            public HttpServletRequest get()
            {
                return null;
            }
        }, tokenService );

        assertFalse( userTokenProvider.get().isPresent() );
    }

    @Test
    public void testParameterPresent()
    {
        when( request.getParameter( "access_token" ) ).thenReturn( "123456" );

        assertEquals( "123456", userTokenProvider.get().get() );

        verify( tokenService, never() ).getToken();
    }

    @Test
    public void testNoParameterPresentAndNoToken()
    {
        when( tokenService.getToken() ).thenReturn( Optional.<String> absent() );

        assertFalse( userTokenProvider.get().isPresent() );
    }

    @Test
    public void testTokenExists()
    {
        when( tokenService.getToken() ).thenReturn( Optional.of( "123456" ) );

        assertEquals( "123456", userTokenProvider.get().get() );
    }
}
