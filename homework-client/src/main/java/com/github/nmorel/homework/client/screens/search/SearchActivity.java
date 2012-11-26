package com.github.nmorel.homework.client.screens.search;

import java.util.logging.Logger;

import com.github.nmorel.homework.client.model.Repositories;
import com.github.nmorel.homework.client.model.Repository;
import com.github.nmorel.homework.client.mvp.ActivityWithPlace;
import com.github.nmorel.homework.client.place.RepoPlace;
import com.github.nmorel.homework.client.place.SearchPlace;
import com.github.nmorel.homework.client.request.RestCallback;
import com.github.nmorel.homework.client.request.SearchRequest;
import com.github.nmorel.homework.client.screens.search.SearchView.Presenter;
import com.github.nmorel.homework.client.ui.State;
import com.google.common.base.Strings;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Activity used to search a repository and visualize a list of them
 * 
 * @author Nicolas Morel
 */
public class SearchActivity
    extends ActivityWithPlace
    implements Presenter
{
    private static final Logger logger = Logger.getLogger( SearchActivity.class.getName() );

    @Inject
    private SearchView view;

    @Inject
    private PlaceController placeController;

    @Inject
    private Provider<SearchRequest> searchRequest;

    private SearchPlace currentPlace;

    @Override
    public void visitPlace( SearchPlace place )
    {
        currentPlace = place;
    }

    @Override
    protected void start( AcceptsOneWidget panel, EventBus eventBus )
    {
        view.init();
        view.setPresenter( this );

        view.setKeyword( currentPlace.getKeyword() );

        panel.setWidget( view );

        if ( Strings.isNullOrEmpty( currentPlace.getKeyword() ) )
        {
            logger.fine( "No keyword => we just show an empty form" );
            view.setState( State.DEFAULT );
        }
        else
        {
            logger.fine( "Keyword present => looking for repos with keyword " + currentPlace.getKeyword() );
            searchRequest.get().fire( currentPlace.getKeyword(), new RestCallback<Repositories>() {

                @Override
                protected void onSuccess( Repositories result )
                {
                    logger.fine( result.getRepositories().length() + " repositories found" );
                    view.showResults( result.getRepositories() );
                    view.setState( State.LOADED );
                }
                
                @Override
                protected void onError( Throwable throwable )
                {
                    // we let the default error handling
                    super.onError( throwable );
                    view.setState( State.ERROR );
                }
            } );
            view.setState( State.LOADING );
        }
    }

    @Override
    public void onSearch( String keyword )
    {
        placeController.goTo( new SearchPlace( keyword ) );
    }

    @Override
    public void onSelectionRepository( Repository repo )
    {
        placeController.goTo( new RepoPlace( repo.getOwner(), repo.getName() ) );
    }

}
