package com.github.nmorel.homework.api.model.parser;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.http.HttpResponse;

public abstract class AbstractHttpResponseParser<T>
    implements HttpResponseParser<T>
{
    private static final Logger logger = LoggerFactory.getLogger( GsonHttpResponseParser.class );

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
