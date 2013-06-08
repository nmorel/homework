package com.github.nmorel.homework.client.screens.unauthorized;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.event.HideEvent;
import com.github.gwtbootstrap.client.ui.event.ShownEvent;
import com.github.nmorel.homework.client.resources.messages.Messages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * A widget shown when the user's token has been revoked and we received a 401 error.
 * 
 * @author Nicolas Morel
 */
public class UnauthorizedViewImpl
    implements UnauthorizedView
{
    private static MyUiBinder uiBinder = GWT.create( MyUiBinder.class );

    interface MyUiBinder
        extends UiBinder<Widget, UnauthorizedViewImpl>
    {
    }

    private HasUiHandlers handlers;

    @UiField( provided = true )
    Messages mess;

    @UiField
    Modal modal;

    @UiField
    Button login;

    @Inject
    public UnauthorizedViewImpl( Messages mess )
    {
        this.mess = mess;
        uiBinder.createAndBindUi( this );
    }

    @UiHandler( "login" )
    void onClickLogin( ClickEvent e )
    {
        handlers.onLogin();
    }

    @UiHandler( "skip" )
    void onClickSkip( ClickEvent e )
    {
        handlers.onSkip();
    }

    @UiHandler( "modal" )
    void onHide( HideEvent e )
    {
        handlers.onClose();
    }

    @UiHandler( "modal" )
    void onShown( ShownEvent e )
    {
        login.setFocus( true );
    }

    @Override
    public void show()
    {
        modal.show();
    }

    @Override
    public void hide()
    {
        modal.hide();
    }

    @Override
    public void toggle()
    {
        modal.toggle();
    }

    @Override
    public void setHasUiHandlers( HasUiHandlers hasUiHandlers )
    {
        this.handlers = hasUiHandlers;
    }
}
