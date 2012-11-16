package com.github.nmorel.homework.api.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path( "repo" )
public class RepositoriesResources
{
    private static final Logger logger = LoggerFactory.getLogger( RepositoriesResources.class );

    @GET
    public String search( @QueryParam( "query" ) String query )
    {
        return query + "test";
    }

}
