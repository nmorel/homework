package com.github.nmorel.homework.client.screens.search;

import java.util.logging.Logger;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.nmorel.homework.client.model.Repository;
import com.github.nmorel.homework.client.ui.AbstractView;
import com.github.nmorel.homework.client.ui.KeyboardEventTranslator;
import com.github.nmorel.homework.client.ui.State;
import com.github.nmorel.homework.client.ui.cell.RepositoryCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Default implementation of the view {@link SearchView}.
 * 
 * @author Nicolas Morel
 */
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
        @Source( "searchResultList.css" )
        ListStyle cellListStyle();
    }

    interface ListStyle
        extends CellList.Style
    {

    }

    interface Binder
        extends UiBinder<Widget, SearchViewImpl>
    {
    }

    private static final Logger logger = Logger.getLogger( SearchViewImpl.class.getName() );

    private static final ListResources listResources = GWT.create( ListResources.class );

    private static final Binder uiBinder = GWT.create( Binder.class );

    private Presenter presenter;

    @UiField
    TextBox keyword;

    @UiField
    Button search;

    @UiField
    DeckPanel resultContainer;

    @UiField( provided = true )
    CellList<Repository> resultList;

    ListDataProvider<Repository> resultDataProvider;

    @Inject
    private Provider<RepositoryCell> repositoryCellProvider;

    @Override
    public void setPresenter( Presenter presenter )
    {
        this.presenter = presenter;
    }

    @Override
    protected Widget initWidget()
    {
        resultList = new CellList<Repository>( repositoryCellProvider.get(), listResources );
        // no pagination
        resultList.setPageSize( Integer.MAX_VALUE );
        // user can navigate and select with keyboard
        resultList.setKeyboardSelectionPolicy( KeyboardSelectionPolicy.ENABLED );

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
        resultList.setSelectionModel( selectionModel,
            DefaultSelectionEventManager.createCustomManager( new KeyboardEventTranslator<Repository>() ) );

        Widget result = uiBinder.createAndBindUi( this );

        // we removed the default style to be able to change them via css
        resultContainer.getElement().getStyle().clearPosition();

        return result;
    }

    @Override
    public void setKeyword( String keyword )
    {
        this.keyword.setValue( keyword );
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
    public void clear()
    {
        keyword.setValue( null );
        resultDataProvider.getList().clear();
    }

    @Override
    public void setState( State state )
    {
        logger.fine( "Updating state to " + state );
        switch ( state )
        {
            case DEFAULT:
                setFormEnabled( true );
                resultContainer.setVisible( false );
                setFocusOnKeyword();
                break;

            case LOADING:
                setFormEnabled( false );
                resultContainer.showWidget( 0 );
                resultContainer.setVisible( true );
                // we set the focus on list to loose it
                setFocusOnList();
                break;

            case LOADED:
                setFormEnabled( true );
                if ( resultDataProvider.getList().isEmpty() )
                {
                    resultContainer.showWidget( 1 );
                    setFocusOnKeyword();
                }
                else
                {
                    resultContainer.showWidget( 3 );
                    setFocusOnList();
                }
                resultContainer.setVisible( true );
                break;
            case ERROR:
                setFormEnabled( true );
                resultContainer.showWidget( 2 );
                setFocusOnKeyword();
                resultContainer.setVisible( true );
                break;
        }
    }

    private void setFormEnabled( boolean enabled )
    {
        keyword.setEnabled( enabled );
        search.setEnabled( enabled );
    }

    private void setFocusOnKeyword()
    {
        keyword.setFocus( true );
        keyword.selectAll();
    }

    private void setFocusOnList()
    {
        resultList.setFocus( true );
        Scheduler.get().scheduleDeferred( new ScheduledCommand() {

            @Override
            public void execute()
            {
                resultList.setFocus( true );
            }
        } );
    }

}
