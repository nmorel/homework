package com.github.nmorel.homework.api.config.jersey;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.nmorel.homework.api.parsers.AbstractHttpResponseParser;

/**
 * A default {@link ExceptionMapper} which writes the error message as plain text in the response body. It is mainly to
 * avoid tomcat writing its own html error page. We can improve this by creating a custom exception containing a proper
 * message.
 * 
 * @author Nicolas Morel
 */
@Provider
public class DefaultExceptionMapper
    implements ExceptionMapper<Throwable>
{
    private static final Logger logger = LoggerFactory.getLogger( AbstractHttpResponseParser.class );

    @Override
    public Response toResponse( Throwable exception )
    {
        if ( exception instanceof WebApplicationException )
        {
            Response response = ( (WebApplicationException) exception ).getResponse();
            Status status = Status.fromStatusCode( response.getStatus() );
            String message;
            if ( null == status )
            {
                message = Status.INTERNAL_SERVER_ERROR.getReasonPhrase();
            }
            else
            {
                message = status.getReasonPhrase();
            }

            return Response.fromResponse( response ).type( MediaType.TEXT_PLAIN ).entity( message ).build();
        }
        else
        {
            // Unexpected error
            logger.error( "Unexpected error occured", exception );
            String message = exception.getMessage();
            if ( null == message )
            {
                message = Status.INTERNAL_SERVER_ERROR.getReasonPhrase();
            }
            return Response.serverError().type( MediaType.TEXT_PLAIN ).entity( message ).build();
        }
    }
}
