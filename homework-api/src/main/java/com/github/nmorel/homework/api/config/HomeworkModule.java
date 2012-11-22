package com.github.nmorel.homework.api.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.github.nmorel.homework.api.services.GithubService;
import com.github.nmorel.homework.api.services.OAuthTokenService;
import com.github.nmorel.homework.api.services.UserService;
import com.github.nmorel.homework.api.services.impl.GithubServiceImpl;
import com.github.nmorel.homework.api.services.impl.OAuthTokenServiceImpl;
import com.github.nmorel.homework.api.services.impl.UserServiceImpl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * Guice application module
 * 
 * @author Nicolas Morel
 */
public class HomeworkModule
    extends AbstractModule
{

    @Override
    protected void configure()
    {
        // Http client
        bind( HttpTransport.class ).to( NetHttpTransport.class ).in( Singleton.class );

        // Services
        bind( OAuthTokenService.class ).to( OAuthTokenServiceImpl.class ).in( Singleton.class );
        bind( UserService.class ).to( UserServiceImpl.class ).in( Singleton.class );
        bind( GithubService.class ).to( GithubServiceImpl.class ).in( Singleton.class );
    }

    @Provides
    @Singleton
    public Config providesConfig()
    {
        // this property allow to switch easily the config file used for development or test
        String configFile = System.getProperty( "homework.config", "default.config" );

        // reading the config file
        InputStream inputStream = this.getClass().getResourceAsStream( configFile );
        if ( null == inputStream )
        {
            // config file not found in classpath. We try to read it via File if we gave a path
            try
            {
                inputStream = new FileInputStream( configFile );
            }
            catch ( FileNotFoundException e )
            {
                throw new RuntimeException( "Couldn't find the config file " + configFile );
            }
        }
        InputStreamReader inputStreamReader = new InputStreamReader( inputStream );
        return new Gson().fromJson( inputStreamReader, Config.class );
    }

}
