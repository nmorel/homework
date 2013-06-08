package com.github.nmorel.homework.server;

import com.github.nmorel.homework.api.config.providers.UserIdProvider;
import com.github.nmorel.homework.api.model.User;
import com.github.nmorel.homework.api.services.UserService;
import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.UUID;

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
        // Entry point of the application, we generate here an id for the user that we store in a cookie. This id will
        // allow us to associate a github access token to a user
        boolean alreadyDefined = false;
        if ( null != request.getCookies() )
        {
            for ( Cookie cookie : request.getCookies() )
            {
                if ( UserIdProvider.USER_ID.equals( cookie.getName() ) )
                {
                    alreadyDefined = true;
                    break;
                }
            }
        }
        if ( !alreadyDefined )
        {
            Cookie userId = new Cookie( UserIdProvider.USER_ID, UUID.randomUUID().toString() );
            logger.info( "The user_id wasn't define yet for this user, we set it to {}", userId.getValue() );
            response.addCookie( userId );
        }
    }

    /**
     * Write the content of the page into the response
     * 
     * @throws ServletException
     */
    private void writePage( HttpServletRequest request, HttpServletResponse response )
        throws IOException, ServletException
    {
        response.setContentType( "text/html" );
        response.setCharacterEncoding( DEFAULT_CHARSET.name() );

        PrintWriter writer = response.getWriter();
        writer.print( DEFAULT_DOCTYPE );
        writer.print( "<html>" );
        writer.print( "<head>" );
        writeHead( request, response, writer );
        writer.print( "</head>" );
        writer.print( "<body>" );
        writeBody( request, response, writer );
        writer.print( "</body>" );
        writer.print( "</html>" );
    }

    /**
     * Write the head element
     * 
     * @throws IOException
     * @throws ServletException
     */
    private void writeHead( HttpServletRequest request, HttpServletResponse response, PrintWriter writer )
        throws ServletException, IOException
    {
        writeEncodage( request, response, writer );
        writeTitle( request, response, writer );
        writeCss( request, response, writer );
        writeGWTJavascript( request, response, writer );
        writeVariables( request, response, writer );
    }

    private void writeCss( HttpServletRequest request, HttpServletResponse response, PrintWriter writer )
    {
        addStyleSheet( "/" + getModuleName() + "/css/bootstrap.min.css", writer );
        addStyleSheet( "/" + getModuleName() + "/css/gwt-bootstrap.css", writer );
        addStyleSheet( "/" + getModuleName() + "/css/font-awesome.css", writer );
    }

    private void addStyleSheet( String stylesheet, PrintWriter writer )
    {
        writer.print( "<link rel=\"stylesheet\" href=\"" + stylesheet + "\" >" );
    }

    /**
     * Write the encoding of the page
     */
    private void writeEncodage( HttpServletRequest request, HttpServletResponse response, PrintWriter writer )
    {
        writer.print( "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=" );
        writer.print( DEFAULT_CHARSET );
        writer.print( "\" />" );
    }

    /**
     * Write the title of the page
     */
    private void writeTitle( HttpServletRequest request, HttpServletResponse response, PrintWriter writer )
    {
        writer.print( "<title>" );
        writer.print( "Homework" );
        writer.print( "</title>" );
    }

    /**
     * Write the GWT javascript module to load
     * 
     * @throws IOException
     * @throws ServletException
     */
    private void writeGWTJavascript( HttpServletRequest request, HttpServletResponse response, PrintWriter writer )
        throws ServletException, IOException
    {
        // Inline the nocache.js to save a roundtrip to the server
        writer.print( "<meta name=gwt:property content='baseUrl=/" + getModuleName() + "/'>" );
        writer.print( "<script type=\"text/javascript\" language=\"javascript\" >" );
        getServletContext().getRequestDispatcher( "/" + getModuleName() + "/" + getModuleName() + ".nocache.js" )
            .include( request, response );
        writer.print( "</script>" );
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
            writer.print( "<script type=\"text/javascript\" language=\"javascript\">" );
            writer.print( "var userInfos=" );
            writer.print( new Gson().toJson( user.get() ) );
            writer.print( ";" );
            writer.print( "</script>" );
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
            .print( "<iframe src=\"javascript:''\" id=\"__gwt_historyFrame\" tabIndex='-1' style=\"position:absolute;width:0;height:0;border:0\"></iframe>" );
    }

    /**
     * Write the loading div that will be shown during the loading of the GWT javascript
     */
    private void writeDivLoad( HttpServletRequest request, HttpServletResponse response, PrintWriter writer )
    {
        writer
            .print( "<div id=\"load\" style=\"position: absolute; width:75%; left: 12.5%; height:20px; top: 50%; margin-top: -10px\">" );
        writer
            .print( "<div class=\"progress progress-striped active\"><div class=\"bar\" style=\"width: 100%;\">Loading...</div></div>" );
        writer.print( "</div>" );
    }

    /**
     * @return the GWT module name
     */
    private String getModuleName()
    {
        return ENTRY_POINT_NAME;
    }
}
