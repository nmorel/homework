package com.github.nmorel.homework.api.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.nmorel.homework.api.model.User;
import com.github.nmorel.homework.api.parsers.GsonHttpResponseParser;
import com.github.nmorel.homework.api.services.GithubService;
import com.github.nmorel.homework.api.services.UserService;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpMethods;
import com.google.common.base.Optional;
import com.google.inject.Inject;

/**
 * Default implementation of {@link UserService}.
 * 
 * @author Nicolas Morel
 */
public class UserServiceImpl
    implements UserService
{
    private static final Logger logger = LoggerFactory.getLogger( UserServiceImpl.class );

    private final GithubService githubService;

    @Inject
    public UserServiceImpl( GithubService githubService )
    {
        this.githubService = githubService;
    }

    @Override
    public Optional<User> getAuthenticatedUserInformations()
    {
        logger.debug( "Retrieving informations about the authenticated user" );

        Optional<GenericUrl> urlOpt = githubService.newAuthenticatedGithubUrl();

        if ( urlOpt.isPresent() )
        {
            GenericUrl url = urlOpt.get();
            url.appendRawPath( "/user" );
            try
            {
                User user =
                    githubService.execute( HttpMethods.GET, url, new GsonHttpResponseParser<>( User.class ), true );
                logger.debug( "User found : {}", user );
                return Optional.of( user );
            }
            catch ( Exception e )
            {
                // an exception occured, probably the revokation of the token. We just return an empty result because
                // this service is used by the EntryPointServlet and we don't want to show an error page
                logger
                    .info( "An error occured while retrieving the informations of the authenticated user => returning empty informations" );
                return Optional.absent();
            }
        }
        else
        {
            logger.debug( "Current user isn't authenticated" );
            return Optional.absent();
        }
    }
}
