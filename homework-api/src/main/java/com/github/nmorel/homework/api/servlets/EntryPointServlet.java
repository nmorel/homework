package com.github.nmorel.homework.api.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.nmorel.homework.api.model.User;
import com.github.nmorel.homework.api.services.UserService;
import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.google.inject.Injector;

/**
 * This servlet generates the html page that will host the GWT module
 * 
 * @author Nicolas Morel
 */
public class EntryPointServlet
    extends HttpServlet
{
    private static final long serialVersionUID = -8919133137780291060L;

    private static final String ENTRY_POINT_NAME = "homework";

    private static final Logger logger = LoggerFactory.getLogger( EntryPointServlet.class );

    private static final String DEFAULT_DOCTYPE = "<!doctype html>";

    private static final Charset DEFAULT_CHARSET = Charsets.UTF_8;

    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
        throws ServletException, IOException
    {
        generatesUserIdIfNeeded( request, response );

        logger.debug( "Writing the entry point page" );
        writePage( request, response );
        logger.debug( "Entry point page wrote" );
    }

    /**
     * Generates a cookie with user id if it doesn't already exist
     */
    private void generatesUserIdIfNeeded( HttpServletRequest request, HttpServletResponse response )
    {
        // Entry point of the application, we generate here a id for the user taht we store in a cookie. This id will
        // allow us to associate a github access token to a user
        boolean alreadyDefined = false;
        if ( null != request.getCookies() )
        {
            for ( Cookie cookie : request.getCookies() )
            {
                if ( CookieUtil.USER_ID.equals( cookie.getName() ) )
                {
                    alreadyDefined = true;
                    break;
                }
            }
        }
        if ( !alreadyDefined )
        {
            Cookie userId = new Cookie( CookieUtil.USER_ID, UUID.randomUUID().toString() );
            logger.info( "The user_id wasn't define yet for this user, we set it to {}", userId.getValue() );
            response.addCookie( userId );
        }
    }

    /**
     * Write the content of the page into the response
     */
    private void writePage( HttpServletRequest request, HttpServletResponse response )
        throws IOException
    {
        response.setContentType( "text/html" );
        response.setCharacterEncoding( DEFAULT_CHARSET.name() );

        PrintWriter writer = response.getWriter();
        writer.println( DEFAULT_DOCTYPE );
        writer.println( "<html>" );
        writer.println( "<head>" );
        writeHead( request, response, writer );
        writer.println( "</head>" );
        writer.println( "<body>" );
        writeBody( request, response, writer );
        writer.println( "</body>" );
        writer.println( "</html>" );
    }

    /**
     * Write the head element
     */
    private void writeHead( HttpServletRequest request, HttpServletResponse response, PrintWriter writer )
    {
        writeEncodage( request, response, writer );
        writeTitle( request, response, writer );
        writeGWTJavascript( request, response, writer );
        writeVariables( request, response, writer );
    }

    /**
     * Write the encoding of the page
     */
    private void writeEncodage( HttpServletRequest request, HttpServletResponse response, PrintWriter writer )
    {
        writer.print( "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=" );
        writer.print( DEFAULT_CHARSET );
        writer.println( "\" />" );
    }

    /**
     * Write the title of the page
     */
    private void writeTitle( HttpServletRequest request, HttpServletResponse response, PrintWriter writer )
    {
        writer.print( "<title>" );
        writer.print( "Homework" );
        writer.println( "</title>" );
    }

    /**
     * Write the GWT javascript module to load
     */
    private void writeGWTJavascript( HttpServletRequest request, HttpServletResponse response, PrintWriter writer )
    {
        writer.print( "<script type=\"text/javascript\" language=\"javascript\" src=\"" );
        writer.print( getModuleName() );
        writer.print( "/" );
        writer.print( getModuleName() );
        writer.println( ".nocache.js\"></script>" );
    }

    /**
     * Write javascript variables that can be access from client
     */
    private void writeVariables( HttpServletRequest request, HttpServletResponse response, PrintWriter writer )
    {
        // we write the user infos into a javascript variable to save a roundtrip
        Injector inj = (Injector) getServletContext().getAttribute( Injector.class.getName() );
        UserService userService = inj.getInstance( UserService.class );
        Optional<User> user = userService.getAuthenticatedUserInformations();
        if ( user.isPresent() )
        {
            writer.println( "<script type=\"text/javascript\" language=\"javascript\">" );
            writer.print( "var userInfos=" );
            writer.print( new Gson().toJson( user.get() ) );
            writer.println( ";" );
            writer.println( "</script>" );
        }

    }

    /**
     * Write the body element
     */
    private void writeBody( HttpServletRequest request, HttpServletResponse response, PrintWriter writer )
    {
        writeHistoryHandler( request, response, writer );
        writeDivLoad( request, response, writer );
    }

    /**
     * Write the history handler for history support in GWT
     */
    private void writeHistoryHandler( HttpServletRequest request, HttpServletResponse response, PrintWriter writer )
    {
        writer
            .println( "<iframe src=\"javascript:''\" id=\"__gwt_historyFrame\" tabIndex='-1' style=\"position:absolute;width:0;height:0;border:0\"></iframe>" );
    }

    /**
     * Write the loading div that will be shown during the loading of the GWT javascript
     */
    private void writeDivLoad( HttpServletRequest request, HttpServletResponse response, PrintWriter writer )
    {
        writer
            .println( "<div id=\"load\" style=\"position: absolute; width:100px; left: 50%; margin-left: -50px; height:100px; top: 50%; margin-top: -50px\">" );
        writer.print( "<img src=\"" );
        writer.println( "wait.gif\" />" );
        writer.println( "</div>" );
    }

    /**
     * @return the GWT module name
     */
    private String getModuleName()
    {
        return ENTRY_POINT_NAME;
    }
}
