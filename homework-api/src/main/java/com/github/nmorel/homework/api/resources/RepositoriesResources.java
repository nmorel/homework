package com.github.nmorel.homework.api.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.nmorel.homework.api.model.Commit;
import com.github.nmorel.homework.api.model.User;
import com.github.nmorel.homework.api.parsers.CommitsParser;
import com.github.nmorel.homework.api.parsers.GsonHttpResponseParser;
import com.github.nmorel.homework.api.parsers.StreamingHttpResponseParser;
import com.github.nmorel.homework.api.services.GithubService;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpMethods;
import com.google.common.base.Strings;
import com.google.inject.Inject;

/**
 * Repositories resources. Allow to search and list commits or collaborators.
 * 
 * @author Nicolas Morel
 */
@Path( "repos" )
@Produces( MediaType.APPLICATION_JSON )
public class RepositoriesResources
{
    private static final Logger logger = LoggerFactory.getLogger( RepositoriesResources.class );

    private final GithubService githubService;

    @Inject
    public RepositoriesResources( GithubService githubService )
    {
        this.githubService = githubService;
    }

    /**
     * Looks for repository corresponding to the given keyword
     * 
     * @param keyword
     * @return list of repository corresponding to the keyword
     */
    @GET
    @Path( "search" )
    public StreamingOutput search( @QueryParam( "keyword" ) String keyword )
    {
        if ( Strings.isNullOrEmpty( keyword ) )
        {
            logger.warn( "keyword is mandatory" );
            throw new WebApplicationException( Status.BAD_REQUEST );
        }

        logger.info( "Looking for repositories with the keyword '{}'", keyword );

        GenericUrl url = githubService.newGithubUrl();
        url.appendRawPath( "/legacy/repos/search/" );
        url.appendRawPath( keyword );

        // we directly stream the result since we don't change anything to it nor cache it
        return githubService.execute( HttpMethods.GET, url, new StreamingHttpResponseParser(), false );
    }

    /**
     * List the collaborators of the given repository
     * 
     * @param owner owner of the repository
     * @param repo name of the repository
     * @return the list of all the collaborators of the repository
     */
    @GET
    @Path( "{owner}/{repo}/collaborators" )
    public User[] listCollaborators( @PathParam( "owner" ) String owner, @PathParam( "repo" ) String repo )
    {
        logger.info( "Looking for collaborators to the repository {}/{}", owner, repo );

        GenericUrl url = githubService.newGithubUrl();
        url.appendRawPath( "/repos/" );
        url.appendRawPath( owner );
        url.appendRawPath( "/" );
        url.appendRawPath( repo );
        url.appendRawPath( "/collaborators" );

        User[] collaborators =
            githubService.execute( HttpMethods.GET, url, new GsonHttpResponseParser<>( User[].class ), true );

        logger.info( "{} collaborators found", collaborators.length );

        return collaborators;
    }

    /**
     * List the last 100 commits of the given repository
     * 
     * @param owner owner of the repository
     * @param repo name of the repository
     * @return a list of the last 100 commits of the repository
     */
    @GET
    @Path( "{owner}/{repo}/commits" )
    public List<Commit> listCommits( @PathParam( "owner" ) String owner, @PathParam( "repo" ) String repo )
    {
        logger.info( "Retrieving the informations for the repository : {}/{}", owner, repo );

        GenericUrl url = githubService.newGithubUrl();
        url.appendRawPath( "/repos/" );
        url.appendRawPath( owner );
        url.appendRawPath( "/" );
        url.appendRawPath( repo );
        url.appendRawPath( "/commits" );
        url.set( "per_page", 100 );

        List<Commit> commits = githubService.execute( HttpMethods.GET, url, new CommitsParser(), true );

        logger.info( "{} commits found", commits.size() );

        return commits;
    }

}
