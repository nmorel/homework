package com.github.nmorel.homework.api.parsers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.http.HttpResponse;

/**
 * Convenience abstraction to the {@link HttpResponseParser} interface to give some utility methods to the
 * implementations
 * 
 * @author Nicolas Morel
 * @param <T> type of the result
 */
public abstract class AbstractHttpResponseParser<T>
    implements HttpResponseParser<T>
{
    private static final Logger logger = LoggerFactory.getLogger( GsonHttpResponseParser.class );

    /**
     * Try to close the response
     * 
     * @param response response from github api
     */
    protected void closeResponse( HttpResponse response )
    {
        try
        {
            response.disconnect();
        }
        catch ( IOException e )
        {
            logger.warn( "Error disconnecting the response", e );
        }
    }

}
