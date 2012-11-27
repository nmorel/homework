package com.github.nmorel.homework.client;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.nmorel.homework.client.gin.HomeworkGinjector;
import com.github.nmorel.homework.client.ui.VisualizationLoader;
import com.github.nmorel.homework.client.utils.Alert;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.debug.client.DebugInfo;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.UmbrellaException;

/**
 * Homework entry point
 */
public class Homework
    implements EntryPoint, UncaughtExceptionHandler, ResizeHandler
{
    private static final int MIN_WIDTH = 800;
    private static final int MIN_HEIGHT = 600;

    public static final HomeworkGinjector ginjector = GWT.create( HomeworkGinjector.class );

    private static final Logger logger = Logger.getLogger( Homework.class.getName() );

    private final SimplePanel container = new SimplePanel();

    private final ScrollPanel scroll = new ScrollPanel( container );

    /**
     * Command used to schedule a refresh in layout (change of the client size)
     */
    private final ScheduledCommand layoutCmd = new ScheduledCommand() {
        public void execute()
        {
            layoutScheduled = false;
            forceLayout();
        }
    };
    private boolean layoutScheduled = false;

    /**
     * This is the entry point method.
     */
    public void onModuleLoad()
    {
        GWT.setUncaughtExceptionHandler( this );
        Window.addResizeHandler( this );
        DebugInfo.setDebugIdPrefix( "" );

        initLog();

        // We directly load the visualization api to gain a few ms
        VisualizationLoader.load();

        Window.enableScrolling( false );
        container.setWidget( ginjector.getMainPresenter().getView() );
        RootPanel.get().add( scroll );
        forceLayout();

        // Goes to place represented on URL or default place
        ginjector.getPlaceHistoryHandler().handleCurrentHistory();

        DOM.removeChild( RootPanel.getBodyElement(), DOM.getElementById( "load" ) );
    }

    private void initLog()
    {
        // initialize logger with custom formatter
        Logger rootLogger = Logger.getLogger( "" );
        Handler handlers[] = rootLogger.getHandlers();
        for ( Handler handler : handlers )
        {
            handler.setFormatter( new CustomLogFormatter( true ) );
        }
    }

    @Override
    public void onUncaughtException( Throwable e )
    {
        Throwable throwable = e;
        if ( throwable instanceof UmbrellaException && ( (UmbrellaException) throwable ).getCauses().size() == 1 )
        {
            throwable = ( (UmbrellaException) throwable ).getCauses().iterator().next();
        }
        else if ( throwable instanceof com.google.web.bindery.event.shared.UmbrellaException
            && ( (com.google.web.bindery.event.shared.UmbrellaException) throwable ).getCauses().size() == 1 )
        {
            throwable =
                ( (com.google.web.bindery.event.shared.UmbrellaException) throwable ).getCauses().iterator().next();
        }

        logger.log( Level.SEVERE, "Uncaught exception", throwable );
        Alert.showError( throwable.getMessage() );
    }

    @Override
    public void onResize( ResizeEvent event )
    {
        scheduledLayout();
    }

    /**
     * Schedule layout to adjust the size of the content area.
     */
    private void scheduledLayout()
    {
        if ( !layoutScheduled )
        {
            layoutScheduled = true;
            Scheduler.get().scheduleDeferred( layoutCmd );
        }
    }

    /**
     * Update the size of the container
     */
    private void forceLayout()
    {
        int scrollWidth = Window.getClientWidth();
        int scrollHeight = Window.getClientHeight();

        logger.finer( "Window resized : width=" + scrollWidth + " / height=" + scrollHeight );

        int containerWidth = Math.max( scrollWidth, MIN_WIDTH );
        int containerHeight = Math.max( scrollHeight, MIN_HEIGHT );

        if ( scrollWidth < MIN_WIDTH )
        {
            containerHeight = containerHeight - 17;
        }
        if ( scrollHeight < MIN_HEIGHT )
        {
            containerWidth = containerWidth - 17;
        }

        logger.finer( "Scroll size : width=" + scrollWidth + " / height=" + scrollHeight );
        logger.finer( "Container size : width=" + containerWidth + " / height=" + containerHeight );

        scroll.setPixelSize( scrollWidth, scrollHeight );
        container.setPixelSize( containerWidth, containerHeight );

        Widget child = container.getWidget();
        if ( child instanceof RequiresResize )
        {
            ( (RequiresResize) child ).onResize();
        }
    }
}
