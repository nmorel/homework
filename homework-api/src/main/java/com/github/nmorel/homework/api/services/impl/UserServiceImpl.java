package com.github.nmorel.homework.api.services.impl;

import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.nmorel.homework.api.config.Config;
import com.github.nmorel.homework.api.config.UserId;
import com.github.nmorel.homework.api.model.User;
import com.github.nmorel.homework.api.services.OAuthTokenService;
import com.github.nmorel.homework.api.services.UserService;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class UserServiceImpl
    implements UserService
{
    private static final Logger logger = LoggerFactory.getLogger( UserServiceImpl.class );

    @Inject
    private Config config;

    @Inject
    @UserId
    private Provider<Optional<String>> userIdProvider;

    @Inject
    private HttpTransport httpTransport;

    @Inject
    private OAuthTokenService tokenService;

    @Override
    public Optional<User> getAuthenticatedUserInformations()
    {
        String userId = userIdProvider.get().orNull();
        Optional<String> token = tokenService.getToken( userId );
        if ( token.isPresent() )
        {
            GenericUrl url = new GenericUrl( config.getGithubApiBaseUrl() + "/user" );
            url.set( "access_token", token.get() );
            try
            {
                HttpResponse response = httpTransport.createRequestFactory().buildGetRequest( url ).execute();
                User user = new Gson().fromJson( new InputStreamReader( response.getContent() ), User.class );
                return Optional.fromNullable( user );
            }
            catch ( Exception e )
            {
                // TODO quoi faire en cas d'exception ?
                logger.error( "Error while retrieving the information of the authenticated user {}", userId, e );
            }
        }
        return Optional.absent();
    }
}
