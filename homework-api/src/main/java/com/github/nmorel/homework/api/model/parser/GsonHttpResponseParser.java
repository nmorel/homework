package com.github.nmorel.homework.api.model.parser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import com.google.api.client.http.HttpResponse;
import com.google.common.base.Charsets;
import com.google.gson.Gson;

/**
 * An implementation of {@link HttpResponseParser} which parse the content of the response with {@link Gson} and return
 * an instance of the specified type.
 * 
 * @author Nicolas Morel
 * @param <T> Result type
 */
public class GsonHttpResponseParser<T>
    extends AbstractHttpResponseParser<T>
{

    private Class<T> clazz;

    private Type type;

    public GsonHttpResponseParser( Class<T> clazz )
    {
        this.clazz = clazz;
    }

    public GsonHttpResponseParser( Type type )
    {
        this.type = type;
    }

    @Override
    public T parseResponse( final HttpResponse response )
        throws IOException
    {
        T result;
        Gson gson = new Gson();
        if ( null != clazz )
        {
            result = gson.fromJson( new InputStreamReader( response.getContent(), Charsets.UTF_8 ), clazz );
        }
        else
        {
            result = gson.fromJson( new InputStreamReader( response.getContent(), Charsets.UTF_8 ), type );
        }

        closeResponse( response );

        return result;
    }

}
