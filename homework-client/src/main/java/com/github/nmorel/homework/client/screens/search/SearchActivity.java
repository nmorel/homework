package com.github.nmorel.homework.client.screens.search;

import com.github.nmorel.homework.client.mvp.ActivityWithPlace;
import com.github.nmorel.homework.client.screens.search.SearchView.Presenter;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class SearchActivity
    extends ActivityWithPlace
    implements Presenter
{
    private final SearchView view;

    @Inject
    public SearchActivity( SearchView view )
    {
        this.view = view;
    }

    @Override
    protected void start( AcceptsOneWidget panel, EventBus eventBus )
    {
        view.init();
        view.setPresenter( this );
        panel.setWidget( view );
    }

}
