package com.github.nmorel.homework.client.screens.error;

import com.github.nmorel.homework.client.mvp.ActivityWithPlace;
import com.github.nmorel.homework.client.place.ErrorPlace;
import com.github.nmorel.homework.client.resources.ResourcesBundle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class ErrorActivity
    extends ActivityWithPlace
{
    private static Binder uiBinder = GWT.create( Binder.class );

    interface Binder
        extends UiBinder<Widget, ErrorActivity>
    {
    }

    private ErrorPlace currentPlace;

    private Widget widget;

    @Inject
    @UiField( provided = true )
    ResourcesBundle res;

    @UiField
    Label errorLabel;

    @Override
    public void visitPlace( ErrorPlace errorPlace )
    {
        this.currentPlace = errorPlace;
    }

    @Override
    protected void start( AcceptsOneWidget panel, EventBus eventBus )
    {
        if ( null == widget )
        {
            widget = uiBinder.createAndBindUi( this );
        }
        errorLabel.setText( currentPlace.getMessage() );
        panel.setWidget( widget );
    }

}
