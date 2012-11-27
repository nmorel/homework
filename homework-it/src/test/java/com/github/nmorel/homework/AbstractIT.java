package com.github.nmorel.homework;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.mockwebserver.MockWebServer;

public class AbstractIT
{
    protected static final String URL_BASE = "http://localhost:8090";

    protected MockWebServer githubMock;

    @Before
    public void startGithubMock()
        throws IOException
    {
        githubMock = new MockWebServer();
        githubMock.play( 8091 );
    }

    @After
    public void shutdownGithubMock()
        throws IOException
    {
        githubMock.shutdown();
    }

    protected String getMockedBody( String file )
        throws IOException
    {
        return Files.toString( getTestFile( file ), Charsets.UTF_8 );
    }

    protected File getTestFile( String file )
    {
        return new File( "src/test/resources", file );
    }

}
