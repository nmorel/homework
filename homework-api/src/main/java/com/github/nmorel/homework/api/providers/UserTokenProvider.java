package com.github.nmorel.homework.api.providers;

import javax.servlet.http.HttpServletRequest;

import com.github.nmorel.homework.api.services.OAuthTokenService;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Provides the token added into http request parameters or associated to the current user
 * 
 * @author Nicolas Morel
 */
public class UserTokenProvider
    implements Provider<Optional<String>>
{
    private final Provider<HttpServletRequest> httpServletRequest;
    private final OAuthTokenService tokenService;

    @Inject
    public UserTokenProvider( final Provider<HttpServletRequest> httpServletRequest,
                              final OAuthTokenService tokenService )
    {
        this.httpServletRequest = httpServletRequest;
        this.tokenService = tokenService;
    }

    @Override
    public Optional<String> get()
    {
        HttpServletRequest currentRequest = httpServletRequest.get();
        if ( null == currentRequest )
        {
            // no parameters and no current user
            return Optional.absent();
        }

        // if the request has an access_token parameter, this one is used over the one associated to user
        String token = currentRequest.getParameter( "access_token" );
        if ( !Strings.isNullOrEmpty( token ) )
        {
            return Optional.of( token );
        }

        // no token given in parameters, we search for one associated to the current user
        return tokenService.getToken();
    }

}
