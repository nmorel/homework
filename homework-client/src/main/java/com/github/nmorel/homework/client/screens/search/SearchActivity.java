package com.github.nmorel.homework.client.screens.search;

import com.github.nmorel.homework.client.mvp.ActivityWithPlace;
import com.github.nmorel.homework.client.request.SearchRequest;
import com.github.nmorel.homework.client.screens.search.SearchView.Presenter;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
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
    private Provider<SearchRequest> searchRequest;

    @Override
    protected void start( AcceptsOneWidget panel, EventBus eventBus )
    {
        view.init();
        view.setPresenter( this );
        panel.setWidget( view );
    }

    @Override
    public void onSearch( String query )
    {
        searchRequest.get().fire( query, new RequestCallback() {

            @Override
            public void onResponseReceived( Request request, Response response )
            {
                Window.alert( response.getText() );
            }

            @Override
            public void onError( Request request, Throwable exception )
            {
                // TODO Auto-generated method stub

            }
        } );
    }

}
