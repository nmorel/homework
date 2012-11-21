package com.github.nmorel.homework.client.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.user.client.Command;
import com.google.gwt.visualization.client.VisualizationUtils;

/**
 * Utility class to handle the loading of google visualization api.
 * 
 * @author Nicolas Morel
 */
public class VisualizationLoader
{
    private static final Logger logger = Logger.getLogger( VisualizationLoader.class.getName() );

    /**
     * Indicates if the api is loaded
     */
    private static boolean loaded;

    /**
     * Pending commands
     */
    private static List<Command> pendingCommands;

    /**
     * Load the api if needed
     */
    public static void load()
    {
        if ( !loaded )
        {
            // we load the api right now to gain a few ms
            VisualizationUtils.loadVisualizationApi( new Runnable() {

                @Override
                public void run()
                {
                    logger.fine( "Google visualization api is loaded" );
                    loaded = true;
                    if ( null != pendingCommands && !pendingCommands.isEmpty() )
                    {
                        for ( Command command : pendingCommands )
                        {
                            command.execute();
                        }
                        pendingCommands = null;
                    }
                }
            } );
        }
    }

    /**
     * @return true if the api is loaded, false otherwise
     */
    public static boolean isLoaded()
    {
        return loaded;
    }

    /**
     * Add a pending command
     * 
     * @param command command to add
     */
    public static void addPendingCommand( Command command )
    {
        if ( loaded )
        {
            command.execute();
        }
        else
        {
            if ( null == pendingCommands )
            {
                pendingCommands = new ArrayList<Command>();
            }
            pendingCommands.add( command );
        }
    }
}
