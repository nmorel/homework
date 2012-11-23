package com.github.nmorel.homework.api.config.providers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Returns the user id bound to current http request
 * 
 * @author Nicolas Morel
 */
public class UserIdProvider
    implements Provider<Optional<String>>
{
    public static final String USER_ID = "user_id";

    private final Provider<HttpServletRequest> httpServletRequest;

    @Inject
    public UserIdProvider( final Provider<HttpServletRequest> httpServletRequest )
    {
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public Optional<String> get()
    {
        HttpServletRequest currentRequest = httpServletRequest.get();
        if ( null == currentRequest )
        {
            return Optional.absent();
        }

        Cookie[] cookies = currentRequest.getCookies();
        if ( null != cookies )
        {
            for ( Cookie cookie : cookies )
            {
                if ( Objects.equal( USER_ID, cookie.getName() ) )
                {
                    return Optional.of( cookie.getValue() );
                }
            }
        }
        
        return Optional.absent();
    }

}
