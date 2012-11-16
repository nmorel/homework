package com.github.nmorel.homework.client.screens.search;

import com.github.nmorel.homework.client.model.Repositories;
import com.github.nmorel.homework.client.model.Repository;
import com.github.nmorel.homework.client.mvp.ActivityWithPlace;
import com.github.nmorel.homework.client.place.RepoPlace;
import com.github.nmorel.homework.client.place.SearchPlace;
import com.github.nmorel.homework.client.request.SearchRequest;
import com.github.nmorel.homework.client.screens.search.SearchView.Presenter;
import com.google.common.base.Strings;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;

public class SearchActivity
    extends ActivityWithPlace
    implements Presenter
{
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
        panel.setWidget( view );

        view.setKeyword( currentPlace.getKeyword() );
        if ( Strings.isNullOrEmpty( currentPlace.getKeyword() ) )
        {
            // if there is no keyword, we put the focus on the keyword box
            view.setFocusOnKeyword();
        }
        else
        {
            searchRequest.get().fire( currentPlace.getKeyword(), new RequestCallback() {

                @Override
                public void onResponseReceived( Request request, Response response )
                {
                    Repositories repos = JsonUtils.safeEval( response.getText() );
                    view.showResults( repos.getRepositories() );
                }

                @Override
                public void onError( Request request, Throwable exception )
                {
                    // TODO Auto-generated method stub

                }
            } );
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
