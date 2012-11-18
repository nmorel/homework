package com.github.nmorel.homework.api.model.parser;

import java.io.IOException;

import com.google.api.client.http.HttpResponse;

/**
 * Used to parse the content of {@link HttpResponse}. Implementations must take care of
 * {@link HttpResponse#disconnect()}.
 * 
 * @author Nicolas Morel
 * @param <T>
 */
public interface HttpResponseParser<T>
{
    T parseResponse( final HttpResponse response )
        throws IOException;
}
