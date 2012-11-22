package com.github.nmorel.homework.api.config.providers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.nmorel.homework.api.config.providers.UserIdProvider;
import com.google.inject.Provider;

@RunWith( MockitoJUnitRunner.class )
public class UserIdProviderTest
{

    @Mock
    HttpServletRequest request;

    @Test
    public void testNullRequest()
    {
        UserIdProvider userIdProvider = new UserIdProvider( new Provider<HttpServletRequest>() {

            @Override
            public HttpServletRequest get()
            {
                return null;
            }
        } );

        assertFalse( userIdProvider.get().isPresent() );
    }

    @Test
    public void testNoCookies()
    {
        when( request.getCookies() ).thenReturn( null );

        UserIdProvider userIdProvider = new UserIdProvider( new Provider<HttpServletRequest>() {

            @Override
            public HttpServletRequest get()
            {
                return request;
            }
        } );

        assertFalse( userIdProvider.get().isPresent() );
    }

    @Test
    public void testNoCookieUserId()
    {
        when( request.getCookies() )
            .thenReturn(
                new Cookie[] { new Cookie( "test1", "test" ), new Cookie( "test2", "test" ),
                    new Cookie( "test3", "test" ) } );

        UserIdProvider userIdProvider = new UserIdProvider( new Provider<HttpServletRequest>() {

            @Override
            public HttpServletRequest get()
            {
                return request;
            }
        } );

        assertFalse( userIdProvider.get().isPresent() );
    }

    @Test
    public void testCookieUserId()
    {
        when( request.getCookies() ).thenReturn(
            new Cookie[] { new Cookie( "test1", "test" ), new Cookie( "user_id", "123456" ),
                new Cookie( "test3", "test" ), new Cookie( "user_id", "98765" ) } );

        UserIdProvider userIdProvider = new UserIdProvider( new Provider<HttpServletRequest>() {

            @Override
            public HttpServletRequest get()
            {
                return request;
            }
        } );

        assertEquals( "123456", userIdProvider.get().get() );
    }
}
