package com.github.nmorel.homework.api.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.nmorel.homework.api.config.Config;
import com.github.nmorel.homework.api.services.OAuthTokenService;
import com.google.api.client.http.GenericUrl;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Servlet used as redirection url for the github authorization. With the code given by github, it retrieves the token
 * and associates it to the user. It then redirects to the entry point servlet.
 * 
 * @author Nicolas Morel
 */
@Singleton
public class AuthorizationRedirectionServlet
    extends HttpServlet
{
    private static final long serialVersionUID = 2411221581665029485L;

    private static final Logger logger = LoggerFactory.getLogger( AuthorizationRedirectionServlet.class );

    @Inject
    private Config config;

    @Inject
    private OAuthTokenService tokenService;

    protected void doGet( HttpServletRequest request, HttpServletResponse response )
        throws ServletException, IOException
    {
        String code = request.getParameter( "code" );

        if ( !Strings.isNullOrEmpty( code ) )
        {
            logger
                .debug(
                    "Redirected to this servlet after a successful github authorization. Retrieving the token from code {}",
                    code );
            tokenService.retrieveAndStoreToken( code );
        }

        // Redirect url
        String servletUrl = request.getRequestURL().toString();
        GenericUrl url = new GenericUrl( servletUrl.substring( 0, servletUrl.lastIndexOf( request.getServletPath() ) ) );

        Object gwtDev = request.getParameter( "gwt.codesvr" );
        if ( null != gwtDev )
        {
            url.set( "gwt.codesvr", "127.0.0.1:9997" );
        }

        response.sendRedirect( url.build() );
    }
}
