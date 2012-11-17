package com.github.nmorel.homework.api.config;

import com.github.nmorel.homework.api.resources.RepositoriesResources;
import com.github.nmorel.homework.api.servlets.AuthorizationRedirectionServlet;
import com.github.nmorel.homework.api.servlets.AuthorizationServlet;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class ServletModule
    extends JerseyServletModule
{
    protected void configureServlets()
    {
        // Jersey
        bind( RepositoriesResources.class );
        serve( "/api/*" ).with( GuiceContainer.class );

        // Authentication
        serve( "/authorize" ).with( AuthorizationServlet.class );
        serve( "/redirect" ).with( AuthorizationRedirectionServlet.class );
    }
}
