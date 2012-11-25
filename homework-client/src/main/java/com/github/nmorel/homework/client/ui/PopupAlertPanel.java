package com.github.nmorel.homework.client.ui;

import com.github.gwtbootstrap.client.ui.base.AlertBase;
import com.github.gwtbootstrap.client.ui.event.ClosedEvent;
import com.github.gwtbootstrap.client.ui.event.ClosedHandler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;

public class PopupAlertPanel
    extends PopupPanel
{
    private FlowPanel container;

    public PopupAlertPanel()
    {
        super( false, false );

        container = new FlowPanel();
        setWidget( container );
    }

    public void addAlert( final AlertBase alert )
    {
        container.insert( alert, 0 );

        // adding a timer to automatically hide it after a few seconds
        final Timer timer = new Timer() {

            @Override
            public void run()
            {
                alert.close();
            }
        };
        timer.schedule( 10000 );

        // removing the alert from the container when it's closed
        alert.addClosedHandler( new ClosedHandler() {

            @Override
            public void onClosed( ClosedEvent closedEvent )
            {
                timer.cancel();
                container.remove( alert );
                if ( container.getWidgetCount() == 0 )
                {
                    PopupAlertPanel.this.hide();
                }
            }
        } );

        if ( !isShowing() )
        {
            setPopupPositionAndShow( new PositionCallback() {

                @Override
                public void setPosition( int offsetWidth, int offsetHeight )
                {
                    PopupAlertPanel.this.getElement().getStyle().clearTop();
                    PopupAlertPanel.this.getElement().getStyle().clearLeft();
                    PopupAlertPanel.this.getElement().getStyle().setBottom( 5, Unit.PX );
                    PopupAlertPanel.this.getElement().getStyle().setRight( 5, Unit.PX );
                }
            } );
        }
    }
}
