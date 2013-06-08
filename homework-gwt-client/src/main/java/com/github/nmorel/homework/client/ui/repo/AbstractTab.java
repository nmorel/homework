package com.github.nmorel.homework.client.ui.repo;

import java.util.logging.Logger;

import com.github.nmorel.homework.client.model.Commit;
import com.github.nmorel.homework.client.resources.ResourcesBundle;
import com.github.nmorel.homework.client.resources.messages.Messages;
import com.github.nmorel.homework.client.ui.View;
import com.github.nmorel.homework.client.ui.VisualizationLoader;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.inject.Inject;

public abstract class AbstractTab
    extends SimpleLayoutPanel
    implements View, Tab
{
    private static final Logger logger = Logger.getLogger( AbstractTab.class.getName() );

    @Inject
    private Messages messages;

    @Inject
    private ResourcesBundle resources;

    protected JsArray<Commit> commits;

    @Override
    public boolean isInitialized()
    {
        return null != commits;
    }

    @Override
    public void clear()
    {
        commits = null;
        clearChart();
    }

    protected abstract void clearChart();

    @Override
    public void setData( final JsArray<Commit> commits )
    {
        this.commits = commits;
        if ( VisualizationLoader.isLoaded() )
        {
            makeChart();
        }
        else
        {
            logger.fine( "Visualization api isn't loaded yet, we wait before creating the timeline" );
            VisualizationLoader.addPendingCommand( new Command() {

                @Override
                public void execute()
                {
                    makeChart();
                }
            } );
        }
    }

    /**
     * Build the chart. The google chart api is loaded.
     */
    protected abstract void makeChart();

    @UiFactory
    public Messages getMessages()
    {
        return messages;
    }

    @UiFactory
    public ResourcesBundle getResources()
    {
        return resources;
    }
}
