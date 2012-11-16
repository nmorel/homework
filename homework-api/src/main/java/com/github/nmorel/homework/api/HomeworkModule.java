package com.github.nmorel.homework.api;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.inject.AbstractModule;

public class HomeworkModule
    extends AbstractModule
{

    @Override
    protected void configure()
    {
        bind( HttpClient.class ).to( DefaultHttpClient.class );
    }

}
