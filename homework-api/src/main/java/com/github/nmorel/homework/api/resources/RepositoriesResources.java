package com.github.nmorel.homework.api.resources;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.nmorel.homework.api.model.FullCommit;
import com.github.nmorel.homework.api.model.User;
import com.github.nmorel.homework.api.model.parser.GsonHttpResponseParser;
import com.github.nmorel.homework.api.model.parser.StreamingHttpResponseParser;
import com.github.nmorel.homework.api.services.GithubService;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpMethods;
import com.google.common.base.Strings;
import com.google.inject.Inject;

@Path( "repos" )
@Produces( MediaType.APPLICATION_JSON )
public class RepositoriesResources
{
    private static final Logger logger = LoggerFactory.getLogger( RepositoriesResources.class );

    @Inject
    private GithubService githubService;

    @GET
    @Path( "search" )
    public StreamingOutput search( @QueryParam( "keyword" ) String keyword )
        throws IOException
    {
        if ( Strings.isNullOrEmpty( keyword ) )
        {
            throw new WebApplicationException( 400 );
        }

        logger.info( "Looking for repositories with the keyword '{}'", keyword );

        GenericUrl url = githubService.newGithubUrl();
        url.appendRawPath( "/legacy/repos/search/" );
        url.appendRawPath( keyword );

        return githubService.execute( HttpMethods.GET, url, new StreamingHttpResponseParser(), false );
    }

    @GET
    @Path( "{owner}/{repo}/collaborators" )
    public User[] listCollaborators( @PathParam( "owner" ) String owner, @PathParam( "repo" ) String repo )
        throws IOException
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

    @GET
    @Path( "{owner}/{repo}/commits" )
    public FullCommit[] listCommits( @PathParam( "owner" ) String owner, @PathParam( "repo" ) String repo )
        throws IOException
    {
        logger.info( "Retrieving the informations for the repository : {}/{}", owner, repo );

        GenericUrl url = githubService.newGithubUrl();
        url.appendRawPath( "/repos/" );
        url.appendRawPath( owner );
        url.appendRawPath( "/" );
        url.appendRawPath( repo );
        url.appendRawPath( "/commits" );
        url.set( "per_page", 100 );

        FullCommit[] commits =
            githubService.execute( HttpMethods.GET, url, new GsonHttpResponseParser<>( FullCommit[].class ), true );

        logger.info( "{} commits found", commits.length );
        return commits;
    }

}
