package com.github.nmorel.homework.client.mvp;

import java.util.logging.Logger;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author Nicolas Morel
 */
public abstract class ActivityWithPlace
    extends NoOpVisitor
    implements Activity, BasePlace.Visitor
{
    private static final Logger logger = Logger.getLogger( ActivityWithPlace.class.getName() );

    private BasePlace currentPlace;

    public Activity withPlace( BasePlace place )
    {
        currentPlace = place;
        place.accept( this );
        return this;
    }

    protected BasePlace getCurrentPlace()
    {
        return currentPlace;
    }

    @Override
    public void start( AcceptsOneWidget panel, com.google.gwt.event.shared.EventBus eventBus )
    {
        logger.info( "Start activity " + this.getClass().getName() + " with place " + currentPlace );
        start( panel, (EventBus) eventBus );
    }

    protected abstract void start( AcceptsOneWidget panel, EventBus eventBus );

    @Override
    public String mayStop()
    {
        return null;
    }

    @Override
    public void onCancel()
    {
        logger.info( "Cancelled activity " + this.getClass().getName() + " with place " + currentPlace );
    }

    @Override
    public void onStop()
    {
        logger.info( "Stopped activity " + this.getClass().getName() + " with place " + currentPlace );
    }
}
