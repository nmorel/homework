package com.github.nmorel.homework.client.screens.repo;

import com.github.nmorel.homework.client.mvp.ActivityWithPlace;
import com.github.nmorel.homework.client.place.RepoPlace;
import com.github.nmorel.homework.client.screens.repo.RepoView.Presenter;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class RepoActivity
    extends ActivityWithPlace
    implements Presenter
{
    @Inject
    private RepoView view;

    @Inject
    private PlaceController placeController;

    private RepoPlace currentPlace;

    @Override
    public void visitPlace( RepoPlace place )
    {
        currentPlace = place;
    }

    @Override
    protected void start( AcceptsOneWidget panel, EventBus eventBus )
    {
        view.init();
        view.setPresenter( this );
        panel.setWidget( view );
    }

}
