package com.github.nmorel.homework.api.parsers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.core.StreamingOutput;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.api.client.http.HttpResponse;

@RunWith( PowerMockRunner.class )
@PrepareForTest( HttpResponse.class )
public class StreamingHttpResponseParserTest
{
    @Test
    public void parseResponseTestOk()
        throws IOException
    {
        HttpResponse response = PowerMockito.mock( HttpResponse.class );
        OutputStream os = mock( OutputStream.class );

        StreamingOutput result = new StreamingHttpResponseParser().parseResponse( response );
        result.write( os );

        // make sure we download before closing!
        InOrder inOrder = inOrder( response );
        inOrder.verify( response, times( 1 ) ).download( os );
        inOrder.verify( response, times( 1 ) ).disconnect();
    }

    /**
     * Test that we always call the disconnect method on response even in case of an exception
     * 
     * @throws IOException
     */
    @Test
    public void parseResponseTestWithException()
        throws IOException
    {
        HttpResponse response = PowerMockito.mock( HttpResponse.class );
        OutputStream os = mock( OutputStream.class );

        doThrow( new IOException() ).when( response ).download( os );

        StreamingOutput result = new StreamingHttpResponseParser().parseResponse( response );
        try
        {
            result.write( os );
            fail();
        }
        catch ( IOException e )
        {
        }

        // make sure we download before closing!
        InOrder inOrder = inOrder( response );
        inOrder.verify( response, times( 1 ) ).download( os );
        inOrder.verify( response, times( 1 ) ).disconnect();
    }
}
