package com.github.nmorel.homework.api.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path( "test" )
public class TestResources
{
    private static final Logger logger = LoggerFactory.getLogger( TestResources.class );

    @GET
    @Path( "{name}" )
    public String get( @PathParam( "name" ) String name )
    {
        logger.debug( "received : {}", name );
        return "Hello " + name + "!";
    }
}