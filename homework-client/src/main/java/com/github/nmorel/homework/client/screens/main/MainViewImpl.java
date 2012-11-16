package com.github.nmorel.homework.client.screens.main;

import com.github.nmorel.homework.client.ui.AbstractView;
import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class MainViewImpl
    extends AbstractView
    implements MainView
{

    private static Binder uiBinder = GWT.create( Binder.class );

    interface Binder
        extends UiBinder<Widget, MainViewImpl>
    {
    }

    private MainPresenter presenter;

    @UiField
    TextBox keyword;

    @UiField
    SimpleLayoutPanel container;

    @Override
    protected Widget initWidget()
    {
        Widget widget = uiBinder.createAndBindUi( this );
        keyword.getElement().setAttribute( "placeholder", getMessages().headerSearchPlaceholder() );
        return widget;
    }

    @UiHandler( "keyword" )
    void onEnterKeyword( KeyUpEvent event )
    {
        if ( event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !Strings.isNullOrEmpty( keyword.getValue() ) )
        {
            presenter.onSearch( keyword.getText() );
            keyword.setValue( null );
        }
    }

    @Override
    public void setPresenter( MainPresenter presenter )
    {
        this.presenter = presenter;
    }

    @Override
    public AcceptsOneWidget getContainer()
    {
        return container;
    }
}
