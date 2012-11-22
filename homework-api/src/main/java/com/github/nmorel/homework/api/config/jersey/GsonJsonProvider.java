package com.github.nmorel.homework.api.config.jersey;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

/**
 * Simple reader/writer for Jersey and json using Gson
 * 
 * @author Nicolas Morel
 */
@Provider
@Consumes( MediaType.APPLICATION_JSON )
@Produces( MediaType.APPLICATION_JSON )
public class GsonJsonProvider
    implements MessageBodyReader<Object>, MessageBodyWriter<Object>
{

    /**
     * These are classes that we never use for reading (never try to deserialize instances of these types).
     */
    private final static ImmutableSet<Class<?>> unreadableClasses = ImmutableSet.<Class<?>> of( InputStream.class,
        Reader.class );

    /**
     * These are classes that we never use for writing (never try to serialize instances of these types).
     */
    private final static ImmutableSet<Class<?>> unwritableClasses = ImmutableSet.<Class<?>> of( OutputStream.class,
        Writer.class, StreamingOutput.class, Response.class );

    @Override
    public boolean isReadable( Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType )
    {
        for ( Class<?> cls : unreadableClasses )
        {
            if ( cls.isAssignableFrom( type ) )
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object readFrom( Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                            MultivaluedMap<String, String> httpHeaders, InputStream entityStream )
        throws IOException, WebApplicationException
    {
        return new Gson().fromJson( new InputStreamReader( entityStream, Charsets.UTF_8 ), type );
    }

    @Override
    public boolean isWriteable( Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType )
    {
        for ( Class<?> cls : unwritableClasses )
        {
            if ( cls.isAssignableFrom( type ) )
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public long getSize( Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType )
    {
        return -1;
    }

    @Override
    public void writeTo( Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                         MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream )
        throws IOException, WebApplicationException
    {
        final Writer w = new OutputStreamWriter( entityStream, Charsets.UTF_8 );
        final JsonWriter jsw = new JsonWriter( w );
        new Gson().toJson( t, type, jsw );
        jsw.close();
    }

}
