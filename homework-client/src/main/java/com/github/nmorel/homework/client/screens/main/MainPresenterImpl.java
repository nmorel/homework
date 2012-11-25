package com.github.nmorel.homework.client.screens.main;

import com.github.nmorel.homework.client.events.UserChangeEvent;
import com.github.nmorel.homework.client.model.User;
import com.github.nmorel.homework.client.place.SearchPlace;
import com.github.nmorel.homework.client.utils.LoginRedirection;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class MainPresenterImpl
    implements MainPresenter, UserChangeEvent.Handler
{
    private MainView view;

    @Inject
    private PlaceController placeController;

    @Inject
    public MainPresenterImpl( final MainView view, final User user, final EventBus eventBus )
    {
        this.view = view;
        this.view.init();
        this.view.setPresenter( this );
        this.view.setUser( user );
        eventBus.addHandler( UserChangeEvent.getType(), this );
    }

    @Override
    public MainView getView()
    {
        return view;
    }

    @Override
    public void onSearch( String keyword )
    {
        placeController.goTo( new SearchPlace( keyword ) );
    }

    @Override
    public void onLogin()
    {
        LoginRedirection.redirectToGithubAuthorizationPage();
    }

    @Override
    public void onUserChange( UserChangeEvent event )
    {
        view.setUser( event.getUser() );
    }
}
