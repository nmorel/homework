package com.github.nmorel.homework.client.screens.search;

import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.nmorel.homework.client.model.Repository;
import com.github.nmorel.homework.client.ui.AbstractView;
import com.github.nmorel.homework.client.ui.cell.RepositoryCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

public class SearchViewImpl
    extends AbstractView
    implements SearchView
{
    interface ListResources
        extends CellList.Resources
    {
        /**
         * The styles used in this widget.
         */
        @Source( "list.css" )
        ListStyle cellListStyle();
    }

    interface ListStyle
        extends CellList.Style
    {

    }

    private static ListResources listResources = GWT.create( ListResources.class );

    private static Binder uiBinder = GWT.create( Binder.class );

    interface Binder
        extends UiBinder<Widget, SearchViewImpl>
    {
    }

    private Presenter presenter;

    @UiField
    TextBox keyword;

    @UiField( provided = true )
    CellList<Repository> resultList;

    ListDataProvider<Repository> resultDataProvider;

    @Override
    public void setPresenter( Presenter presenter )
    {
        this.presenter = presenter;
    }

    @Override
    protected Widget initWidget()
    {
        resultList = new CellList<Repository>( new RepositoryCell(), listResources );
        resultList.setPageSize( Integer.MAX_VALUE );

        resultDataProvider = new ListDataProvider<Repository>();
        resultDataProvider.addDataDisplay( resultList );

        final SingleSelectionModel<Repository> selectionModel = new SingleSelectionModel<Repository>();
        selectionModel.addSelectionChangeHandler( new Handler() {

            @Override
            public void onSelectionChange( SelectionChangeEvent event )
            {
                presenter.onSelectionRepository( selectionModel.getSelectedObject() );
            }
        } );
        resultList.setSelectionModel( selectionModel );

        return uiBinder.createAndBindUi( this );
    }

    @Override
    public void setKeyword( String keyword )
    {
        this.keyword.setValue( keyword );
    }

    @Override
    public void setFocusOnKeyword()
    {
        this.keyword.setFocus( true );
        this.keyword.selectAll();
    }

    @UiHandler( "keyword" )
    void onEnterKeyword( KeyUpEvent event )
    {
        if ( event.getNativeKeyCode() == KeyCodes.KEY_ENTER )
        {
            presenter.onSearch( keyword.getText() );
        }
    }

    @UiHandler( "search" )
    void onClickSearch( ClickEvent event )
    {
        presenter.onSearch( keyword.getText() );
    }

    @Override
    public void showResults( JsArray<Repository> repos )
    {
        resultDataProvider.getList().clear();
        for ( int i = 0; i < repos.length(); i++ )
        {
            resultDataProvider.getList().add( repos.get( i ) );
        }
    }

    @Override
    protected void clear()
    {
        keyword.setValue( null );
        resultDataProvider.getList().clear();
    }

}
