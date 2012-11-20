package com.github.nmorel.homework.api.model.parser;

import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import com.google.api.client.http.HttpResponse;

/**
 * An implementation of {@link HttpResponseParser} which returns a {@link StreamingOutput}.
 * 
 * @author Nicolas Morel
 */
public class StreamingHttpResponseParser
    extends AbstractHttpResponseParser<StreamingOutput>
{

    @Override
    public StreamingOutput parseResponse( final HttpResponse response )
        throws IOException
    {
        return new StreamingOutput() {

            @Override
            public void write( OutputStream output )
                throws IOException, WebApplicationException
            {
                try
                {
                    response.download( output );
                }
                finally
                {
                    closeResponse( response );
                }
            }
        };
    }

}
