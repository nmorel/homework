package com.github.nmorel.homework.client.screens.repo;

import com.github.nmorel.homework.client.model.Commit;
import com.github.nmorel.homework.client.model.User;
import com.github.nmorel.homework.client.mvp.ActivityWithPlace;
import com.github.nmorel.homework.client.place.RepoPlace;
import com.github.nmorel.homework.client.request.CollaboratorsRequest;
import com.github.nmorel.homework.client.request.CommitsRequest;
import com.github.nmorel.homework.client.screens.repo.RepoView.Presenter;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;

public class RepoActivity
    extends ActivityWithPlace
    implements Presenter
{
    @Inject
    private RepoView view;

    @Inject
    private PlaceController placeController;

    @Inject
    private Provider<CommitsRequest> commitsRequest;

    @Inject
    private Provider<CollaboratorsRequest> collaboratorsRequest;

    private RepoPlace currentPlace;

    @Override
    public void visitPlace( RepoPlace place )
    {
        currentPlace = place;
    }

    @Override
    protected void start( AcceptsOneWidget panel, EventBus eventBus )
    {
        if ( currentPlace.isReload() )
        {
            view.init();
        }
        view.setPresenter( this );

        if ( currentPlace.isReload() )
        {
            commitsRequest.get().fire( currentPlace.getOwner(), currentPlace.getName(), new RequestCallback() {

                @Override
                public void onResponseReceived( Request request, Response response )
                {
                    JsArray<Commit> commits = JsonUtils.safeEval( response.getText() );
                    view.showResults( commits );
                }

                @Override
                public void onError( Request request, Throwable exception )
                {
                    // TODO Auto-generated method stub

                }
            } );
            collaboratorsRequest.get().fire( currentPlace.getOwner(), currentPlace.getName(), new RequestCallback() {

                @Override
                public void onResponseReceived( Request request, Response response )
                {
                    JsArray<User> commits = JsonUtils.safeEval( response.getText() );
                    view.showCollaborators( commits );
                }

                @Override
                public void onError( Request request, Throwable exception )
                {
                    // TODO Auto-generated method stub

                }
            } );
        }

        view.selectTab( currentPlace.getTab() );

        panel.setWidget( view );
    }

    @Override
    public void onTabChange( int tab )
    {
        placeController.goTo( new RepoPlace( currentPlace.getOwner(), currentPlace.getName(), tab, false ) );
    }

}
