package com.github.nmorel.homework.client.screens.main;

import java.util.logging.Logger;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.Tooltip;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.Placement;
import com.github.nmorel.homework.client.model.RecentRepository;
import com.github.nmorel.homework.client.model.User;
import com.github.nmorel.homework.client.ui.AbstractView;
import com.github.nmorel.homework.client.ui.cell.RecentRepositoryCell;
import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class MainViewImpl
    extends AbstractView
    implements MainView
{

    interface ListResources
        extends CellList.Resources
    {
        /**
         * The styles used in this widget.
         */
        @Source( "recentReposList.css" )
        ListStyle cellListStyle();
    }

    interface ListStyle
        extends CellList.Style
    {

    }

    interface Binder
        extends UiBinder<Widget, MainViewImpl>
    {
    }

    private static final Logger logger = Logger.getLogger( MainViewImpl.class.getName() );

    private static final ListResources listResources = GWT.create( ListResources.class );

    private static Binder uiBinder = GWT.create( Binder.class );

    private MainPresenter presenter;

    @UiField
    TextBox keyword;

    @UiField
    SimplePanel container;

    @UiField
    SimplePanel loginPanel;

    @UiField( provided = true )
    CellList<RecentRepository> recentReposList;

    private ListDataProvider<RecentRepository> recentReposListProvider;

    @Inject
    Provider<RecentRepositoryCell> recentRepositoryCellProvider;

    @Override
    protected Widget initWidget()
    {
        recentReposList = new CellList<RecentRepository>( recentRepositoryCellProvider.get(), listResources );
        // doesn't need pagination
        recentReposList.setPageSize( Integer.MAX_VALUE );
        recentReposListProvider = new ListDataProvider<RecentRepository>();
        recentReposListProvider.addDataDisplay( recentReposList );

        Widget widget = uiBinder.createAndBindUi( this );
        keyword.setPlaceholder( getMessages().headerSearchPlaceholder() );

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

    @UiHandler( "search" )
    void onClickSearch( ClickEvent event )
    {
        if ( !Strings.isNullOrEmpty( keyword.getValue() ) )
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

    @Override
    public void setUser( User user )
    {
        if ( null == user )
        {
            Tooltip tooltip = new Tooltip( getMessages().headerLoginButtonTooltip() );
            tooltip.setPlacement( Placement.BOTTOM );

            Button loginButton = new Button( getMessages().headerLoginButton() );
            loginButton.setType( ButtonType.PRIMARY );
            loginButton.addClickHandler( new ClickHandler() {

                @Override
                public void onClick( ClickEvent event )
                {
                    presenter.onLogin();
                }
            } );
            tooltip.add( loginButton );
            loginPanel.setWidget( tooltip );
        }
        else
        {
            HorizontalPanel panel = new HorizontalPanel();
            panel.setHeight( "100%" );
            panel.setVerticalAlignment( HasVerticalAlignment.ALIGN_MIDDLE );
            Image avatar = new Image( user.getAvatarUrl() );
            avatar.setSize( "25px", "25px" );
            avatar.getElement().getStyle().setMarginRight( 10, Unit.PX );

            Label label = new Label( user.getName() );
            panel.add( avatar );
            panel.add( label );
            loginPanel.setWidget( panel );
        }
    }

    @Override
    public void clear()
    {
        // nothing to do
    }

    @Override
    public void updateRecentRepos( JsArray<RecentRepository> recentReposArray )
    {
        if ( null != recentReposArray && recentReposArray.length() > 0 )
        {
            recentReposListProvider.getList().clear();
            for ( int i = 0; i < recentReposArray.length(); i++ )
            {
                RecentRepository repo = recentReposArray.get( i );
                recentReposListProvider.getList().add( repo );
            }
        }
    }
}
