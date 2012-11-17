package com.github.nmorel.homework.api.config;

import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;

import com.github.nmorel.homework.api.services.OAuthTokenService;
import com.github.nmorel.homework.api.services.UserService;
import com.github.nmorel.homework.api.services.impl.OAuthTokenServiceImpl;
import com.github.nmorel.homework.api.services.impl.UserServiceImpl;
import com.github.nmorel.homework.api.servlets.CookieUtil;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;

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

    @Provides
    @UserId
    public String providesUserId( Provider<HttpServletRequest> httpServletRequest )
    {
        return CookieUtil.getFirstCookieValue( httpServletRequest.get().getCookies(), CookieUtil.USER_ID );
    }

}
