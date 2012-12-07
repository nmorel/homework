package com.github.nmorel.homework.client.screens.main;

import com.github.nmorel.homework.client.events.UserChangeEvent;
import com.github.nmorel.homework.client.model.RecentRepository;
import com.github.nmorel.homework.client.model.User;
import com.github.nmorel.homework.client.place.RepoPlace;
import com.github.nmorel.homework.client.place.SearchPlace;
import com.github.nmorel.homework.client.utils.LoginRedirection;
import com.google.common.base.Objects;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.storage.client.Storage;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class MainPresenterImpl
    implements MainPresenter, UserChangeEvent.Handler, PlaceChangeEvent.Handler
{
    private static final String RECENT_REPO_KEY = "recentlySeenRepos";

    private static final int MAX_RECENT_REPO = 40;

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
        if ( Storage.isLocalStorageSupported() )
        {
            eventBus.addHandler( PlaceChangeEvent.TYPE, this );
            String recentlySeenRepos = Storage.getLocalStorageIfSupported().getItem( RECENT_REPO_KEY );
            if ( null != recentlySeenRepos )
            {
                JsArray<RecentRepository> recentReposArray = JsonUtils.safeEval( recentlySeenRepos );
                view.updateRecentRepos( recentReposArray );
            }
        }
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

    @Override
    public void onPlaceChange( PlaceChangeEvent event )
    {
        if ( !( event.getNewPlace() instanceof RepoPlace ) )
        {
            return;
        }

        RepoPlace place = (RepoPlace) event.getNewPlace();
        String recentlySeenRepos = Storage.getLocalStorageIfSupported().getItem( RECENT_REPO_KEY );

        if ( null == recentlySeenRepos )
        {
            JsArray<RecentRepository> recentReposArray = JavaScriptObject.createArray().cast();
            recentReposArray.push( createRecentRepository( place ) );
            Storage.getLocalStorageIfSupported().setItem( "recentlySeenRepos", stringify( recentReposArray ) );
            view.updateRecentRepos( recentReposArray );
        }
        else
        {
            JsArray<RecentRepository> oldReposArray = JsonUtils.safeEval( recentlySeenRepos );
            JsArray<RecentRepository> recentReposArray = JavaScriptObject.createArray().cast();

            recentReposArray.push( createRecentRepository( place ) );

            int size = 1;

            for ( int i = 0; i < oldReposArray.length() && size <= MAX_RECENT_REPO; i++ )
            {
                RecentRepository repo = oldReposArray.get( i );
                if ( !( Objects.equal( repo.getName(), place.getName() ) && Objects.equal( repo.getOwner(),
                    place.getOwner() ) ) )
                {
                    recentReposArray.push( repo );
                    size++;
                }
            }

            Storage.getLocalStorageIfSupported().setItem( RECENT_REPO_KEY, stringify( recentReposArray ) );
            view.updateRecentRepos( recentReposArray );
        }
    }

    private RecentRepository createRecentRepository( RepoPlace place )
    {
        RecentRepository repo = JavaScriptObject.createObject().cast();
        repo.setOwner( place.getOwner() );
        repo.setName( place.getName() );
        return repo;
    }

    private final native String stringify( JavaScriptObject object )
    /*-{
        return JSON.stringify(object);
    }-*/;
}
