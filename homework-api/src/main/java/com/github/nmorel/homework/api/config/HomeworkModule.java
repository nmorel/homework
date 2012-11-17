package com.github.nmorel.homework.api.config;

import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class HomeworkModule
    extends AbstractModule
{

    @Override
    protected void configure()
    {
        bind( HttpTransport.class ).to( NetHttpTransport.class ).in( Singleton.class );
    }

    @Provides
    @Singleton
    public Config providesConfig()
    {
        String configFile = System.getProperty( "homework.config", "default.config" );
        InputStream inputStream = this.getClass().getResourceAsStream( configFile );
        InputStreamReader inputStreamReader = new InputStreamReader( inputStream );
        return new Gson().fromJson( inputStreamReader, Config.class );
    }

}
