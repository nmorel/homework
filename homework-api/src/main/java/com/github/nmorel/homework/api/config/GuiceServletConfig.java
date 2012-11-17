package com.github.nmorel.homework.api.config;

import javax.servlet.ServletContextEvent;

import org.slf4j.bridge.SLF4JBridgeHandler;

import com.github.nmorel.homework.api.resources.RepositoriesResources;
import com.github.nmorel.homework.api.resources.TestResources;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class GuiceServletConfig
    extends GuiceServletContextListener
{

    @Override
    public void contextInitialized( ServletContextEvent servletContextEvent )
    {
        // Optionally remove existing handlers attached to j.u.l root logger
        SLF4JBridgeHandler.removeHandlersForRootLogger();

        // add SLF4JBridgeHandler to j.u.l's root logger, should be done once during
        // the initialization phase of your application
        SLF4JBridgeHandler.install();

        super.contextInitialized( servletContextEvent );
    }

    @Override
    public void contextDestroyed( ServletContextEvent servletContextEvent )
    {
        super.contextDestroyed( servletContextEvent );

        // remove SLF4JBridgeHandler
        SLF4JBridgeHandler.uninstall();
    }

    @Override
    protected Injector getInjector()
    {
        return Guice.createInjector( new HomeworkModule(), new JerseyServletModule() {
            protected void configureServlets()
            {
                bind( TestResources.class );
                bind( RepositoriesResources.class );

                serve( "/api/*" ).with( GuiceContainer.class );
            };
        } );
    }

}
