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
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.web.bindery.event.shared.UmbrellaException;

/**
 * Homework entry point
 */
public class Homework
    implements EntryPoint, UncaughtExceptionHandler
{
    public static final HomeworkGinjector ginjector = GWT.create( HomeworkGinjector.class );

    private static final Logger logger = Logger.getLogger( Homework.class.getName() );

    /**
     * This is the entry point method.
     */
    public void onModuleLoad()
    {
        GWT.setUncaughtExceptionHandler( this );

        initLog();

        // We directly load the visualization api to gain a few ms
        VisualizationLoader.load();

        final SimplePanel panel = new SimplePanel();
        RootPanel.get().add( panel );
        
        panel.setSize( Window.getClientWidth() + "px", Window.getClientHeight() + "px" );
        panel.getElement().getStyle().setPropertyPx( "minWidth", 800 );
        panel.getElement().getStyle().setPropertyPx( "minHeight", 600 );
        panel.setWidget( ginjector.getMainPresenter().getView() );

        Window.addResizeHandler( new ResizeHandler() {

            @Override
            public void onResize( ResizeEvent event )
            {
                panel.setSize( Window.getClientWidth() + "px", Window.getClientHeight() + "px" );
            }
        } );

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
}
