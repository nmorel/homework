package com.github.nmorel.homework.client.screens.search;

import com.github.nmorel.homework.client.model.Repository;
import com.github.nmorel.homework.client.ui.AbstractView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
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

    @UiField
    VerticalPanel resultPanel;

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

    @Override
    public void showResults( JsArray<Repository> repos )
    {
        resultPanel.clear();
        for ( int i = 0; i < repos.length(); i++ )
        {
            resultPanel.add( new Label( repos.get( i ).getName() ) );
        }
    }

}
