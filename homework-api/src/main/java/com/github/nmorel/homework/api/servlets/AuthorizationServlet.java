package com.github.nmorel.homework.api.servlets;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.nmorel.homework.api.config.Config;
import com.google.api.client.http.GenericUrl;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Servlet used to redirect the user to the github authorization page with the correct parameters
 * 
 * @author Nicolas Morel
 */
@Singleton
public class AuthorizationServlet
    extends HttpServlet
{
    private static final long serialVersionUID = 2411221581665029485L;

    private static final Logger logger = LoggerFactory.getLogger( AuthorizationServlet.class );

    @Inject
    private Config config;

    @SuppressWarnings( "unchecked" )
    protected void doGet( HttpServletRequest req, HttpServletResponse resp )
        throws ServletException, IOException
    {
        // Redirect url
        String servletUrl = req.getRequestURL().toString();
        GenericUrl redirectUrl =
            new GenericUrl( servletUrl.substring( 0, servletUrl.lastIndexOf( req.getServletPath() ) ) + "/redirect" );

        for ( Entry<String, String[]> parameter : (Set<Entry<String, String[]>>) req.getParameterMap().entrySet() )
        {
            if ( "hash".equals( parameter.getKey() ) && parameter.getValue().length > 0 )
            {
                redirectUrl.setFragment( parameter.getValue()[0] );
            }
            else if ( parameter.getValue().length > 0 )
            {
                redirectUrl.set( parameter.getKey().toString(), parameter.getValue()[0] );
            }
        }

        GenericUrl url = new GenericUrl( config.getGithubAuthorizeUrl() );
        url.set( "client_id", config.getGithubClientId() );
        url.set( "redirect_uri", redirectUrl.build() );
        url.set( "scope", "public_repo" );

        logger.info( "Redirecting the user to the github authorization page : {}", url.build() );

        resp.sendRedirect( url.build() );
    }
}
