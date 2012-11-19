package com.github.nmorel.homework.client.screens.main;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.Tooltip;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.Placement;
import com.github.nmorel.homework.client.model.User;
import com.github.nmorel.homework.client.ui.AbstractView;
import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
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

    @UiField
    SimplePanel loginPanel;

    @Override
    protected Widget initWidget()
    {
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
}
