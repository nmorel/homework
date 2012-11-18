package com.github.nmorel.homework.api.providers;

import javax.servlet.http.HttpServletRequest;

import com.github.nmorel.homework.api.servlets.CookieUtil;
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
        return Optional
            .fromNullable( CookieUtil.getFirstCookieValue( currentRequest.getCookies(), CookieUtil.USER_ID ) );
    }

}
