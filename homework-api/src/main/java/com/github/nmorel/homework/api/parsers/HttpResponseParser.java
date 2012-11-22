package com.github.nmorel.homework.api.parsers;

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
    /**
     * Parse a response to the github api
     * 
     * @param response response from github api
     * @return the parsed result
     * @throws IOException
     */
    T parseResponse( final HttpResponse response )
        throws IOException;
}
