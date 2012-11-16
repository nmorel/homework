package com.github.nmorel.homework.client.screens.search;

import com.github.nmorel.homework.client.ui.AbstractView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class SearchViewImpl
    extends AbstractView
    implements SearchView
{
    private static Binder uiBinder = GWT.create( Binder.class );

    interface Binder
        extends UiBinder<Widget, SearchViewImpl>
    {
    }

    private Presenter presenter;

    @UiField
    TextBox query;

    @Override
    public void setPresenter( Presenter presenter )
    {
        this.presenter = presenter;
    }

    @Override
    protected Widget initWidget()
    {
        return uiBinder.createAndBindUi( this );
    }

    @UiHandler( "search" )
    void onClickSearch( ClickEvent event )
    {
        presenter.onSearch( query.getText() );
    }

}
