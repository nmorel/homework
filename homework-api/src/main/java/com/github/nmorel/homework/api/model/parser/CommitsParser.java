package com.github.nmorel.homework.api.model.parser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.nmorel.homework.api.model.Commit;
import com.github.nmorel.homework.api.model.Committer;
import com.google.api.client.http.HttpResponse;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

/**
 * Parse the result of the commit listing of a repository
 * 
 * @author Nicolas Morel
 */
public class CommitsParser
    extends AbstractHttpResponseParser<List<Commit>>
{
    private static final Logger logger = LoggerFactory.getLogger( CommitsParser.class );

    @Override
    public List<Commit> parseResponse( HttpResponse response )
        throws IOException
    {
        JsonReader reader = null;
        try
        {
            logger.debug( "Starting parse of the response" );

            reader = new JsonReader( new InputStreamReader( response.getContent() ) );
            reader.setLenient( true );

            // response is an array of objects (commits). If we receive something else, we return an empty list
            if ( !JsonToken.BEGIN_ARRAY.equals( reader.peek() ) )
            {
                logger.warn( "Result isn't an array : {}. Returning an empty list.", reader.peek() );
                return Collections.emptyList();
            }

            List<Commit> result = parseResponse( reader );

            logger.debug( "Response parsed : {} commits", result.size() );
            return result;
        }
        finally
        {
            if ( null != reader )
            {
                try
                {
                    reader.close();
                }
                catch ( IOException e )
                {
                    logger.warn( "Error closing the JsonReader", e );
                }
            }
            closeResponse( response );
        }
    }

    /**
     * Parse the response
     * 
     * @param reader
     * @return
     * @throws IOException
     */
    private List<Commit> parseResponse( JsonReader reader )
        throws IOException
    {
        List<Commit> result = new ArrayList<>();

        reader.beginArray();

        while ( JsonToken.END_ARRAY != reader.peek() )
        {
            Commit commit = new Commit();
            commit.setAuthor( new Committer() );
            commit.setCommitter( new Committer() );

            reader.beginObject();

            while ( JsonToken.NAME.equals( reader.peek() ) )
            {
                String name = reader.nextName();
                switch ( name )
                {
                    case "author":
                        parseTopAuthorOrCommitter( reader, commit.getAuthor() );
                        break;
                    case "commit":
                        parseCommit( reader, commit );
                        break;
                    case "committer":
                        parseTopAuthorOrCommitter( reader, commit.getCommitter() );
                        break;
                    case "sha":
                        commit.setSha( reader.nextString() );
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();

            result.add( commit );
        }

        reader.endArray();
        return result;
    }

    /**
     * Parse a "commit" object
     * 
     * @param reader
     * @param commit
     * @throws IOException
     */
    private void parseCommit( JsonReader reader, Commit commit )
        throws IOException
    {
        reader.beginObject();
        while ( JsonToken.NAME.equals( reader.peek() ) )
        {
            String name = reader.nextName();
            switch ( name )
            {
                case "author":
                    parseInnerAuthorOrCommitter( reader, commit.getAuthor() );
                    break;
                case "committer":
                    parseInnerAuthorOrCommitter( reader, commit.getCommitter() );
                    break;
                case "message":
                    commit.setMessage( reader.nextString() );
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
    }

    /**
     * Parse a "author" or "committer" inside a "commit"
     * 
     * @param reader
     * @param committer
     * @throws IOException
     */
    private void parseInnerAuthorOrCommitter( JsonReader reader, Committer committer )
        throws IOException
    {
        reader.beginObject();
        while ( JsonToken.NAME.equals( reader.peek() ) )
        {
            String name = reader.nextName();
            switch ( name )
            {
                case "date":
                    committer.setDate( reader.nextString() );
                    break;
                case "email":
                    committer.setEmail( reader.nextString() );
                    break;
                case "name":
                    committer.setName( reader.nextString() );
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
    }

    /**
     * Parse a "author" or "committer" at the top of the object
     * 
     * @param reader
     * @param committer
     * @throws IOException
     */
    private void parseTopAuthorOrCommitter( JsonReader reader, Committer committer )
        throws IOException
    {
        // the author or committer element can be null in some case
        if ( !JsonToken.BEGIN_OBJECT.equals( reader.peek() ) )
        {
            reader.skipValue();
            return;
        }

        reader.beginObject();
        while ( JsonToken.NAME.equals( reader.peek() ) )
        {
            String name = reader.nextName();
            switch ( name )
            {
                case "login":
                    committer.setLogin( reader.nextString() );
                    break;
                case "avatar_url":
                    committer.setAvatarUrl( reader.nextString() );
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
    }

}
