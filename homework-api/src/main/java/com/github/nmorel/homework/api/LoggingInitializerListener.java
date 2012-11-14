package com.github.nmorel.homework.api;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.bridge.SLF4JBridgeHandler;

public class LoggingInitializerListener
    implements ServletContextListener
{
    @Override
    public void contextInitialized( ServletContextEvent arg0 )
    {
        // Optionally remove existing handlers attached to j.u.l root logger
        SLF4JBridgeHandler.removeHandlersForRootLogger();

        // add SLF4JBridgeHandler to j.u.l's root logger, should be done once during
        // the initialization phase of your application
        SLF4JBridgeHandler.install();
    }

    @Override
    public void contextDestroyed( ServletContextEvent arg0 )
    {
        SLF4JBridgeHandler.uninstall();
    }

}
