package com.github.nmorel.homework.client.screens.repo;

import com.github.nmorel.homework.client.model.Commit;
import com.github.nmorel.homework.client.model.DetailedRepository;
import com.github.nmorel.homework.client.model.User;
import com.github.nmorel.homework.client.mvp.ActivityWithPlace;
import com.github.nmorel.homework.client.place.RepoPlace;
import com.github.nmorel.homework.client.request.CollaboratorsRequest;
import com.github.nmorel.homework.client.request.CommitsRequest;
import com.github.nmorel.homework.client.request.RepositoryRequest;
import com.github.nmorel.homework.client.request.RestCallback;
import com.github.nmorel.homework.client.screens.repo.RepoView.Presenter;
import com.google.gwt.core.client.JsArray;
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
    private Provider<RepositoryRequest> repositoryRequest;

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
            repositoryRequest.get().fire( currentPlace.getOwner(), currentPlace.getName(),
                new RestCallback<DetailedRepository>() {

                    @Override
                    protected void onSuccess( DetailedRepository result )
                    {
                        view.showRepositoryInformations( result );
                    }
                } );
            commitsRequest.get().fire( currentPlace.getOwner(), currentPlace.getName(),
                new RestCallback<JsArray<Commit>>() {

                    @Override
                    protected void onSuccess( JsArray<Commit> result )
                    {
                        view.showResults( result );
                    }
                } );
            collaboratorsRequest.get().fire( currentPlace.getOwner(), currentPlace.getName(),
                new RestCallback<JsArray<User>>() {

                    @Override
                    protected void onSuccess( JsArray<User> result )
                    {
                        view.showCollaborators( result );
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
