package com.github.nmorel.homework.api.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.nmorel.homework.api.model.User;
import com.github.nmorel.homework.api.model.parser.GsonHttpResponseParser;
import com.github.nmorel.homework.api.services.GithubService;
import com.github.nmorel.homework.api.services.UserService;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpMethods;
import com.google.common.base.Optional;
import com.google.inject.Inject;

public class UserServiceImpl
    implements UserService
{
    private static final Logger logger = LoggerFactory.getLogger( UserServiceImpl.class );

    @Inject
    private GithubService githubService;

    @Override
    public Optional<User> getAuthenticatedUserInformations()
    {
        logger.debug( "Retrieving informations about the authenticated user" );
        
        Optional<GenericUrl> urlOpt = githubService.newAuthenticatedGithubUrl();

        if ( urlOpt.isPresent() )
        {
            GenericUrl url = urlOpt.get();
            url.appendRawPath( "/user" );
            User user = githubService.execute( HttpMethods.GET, url, new GsonHttpResponseParser<>( User.class ), true );
            logger.debug( "User found : {}", user );
            return Optional.of( user );
        }
        else
        {
            logger.debug( "No authenticated user found" );
            return Optional.absent();
        }
    }
}
