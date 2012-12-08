package com.github.nmorel.homework.client.gin;

import com.github.nmorel.homework.client.model.User;
import com.github.nmorel.homework.client.mvp.AppActivityMapper;
import com.github.nmorel.homework.client.mvp.AppPlaceHistoryMapper;
import com.github.nmorel.homework.client.place.TokenEnum;
import com.github.nmorel.homework.client.resources.ResourcesBundle;
import com.github.nmorel.homework.client.resources.messages.Messages;
import com.github.nmorel.homework.client.screens.error.ErrorActivity;
import com.github.nmorel.homework.client.screens.main.MainPresenter;
import com.github.nmorel.homework.client.screens.main.MainPresenterImpl;
import com.github.nmorel.homework.client.screens.main.MainView;
import com.github.nmorel.homework.client.screens.main.MainViewImpl;
import com.github.nmorel.homework.client.screens.repo.RepoView;
import com.github.nmorel.homework.client.screens.repo.RepoViewImpl;
import com.github.nmorel.homework.client.screens.search.SearchView;
import com.github.nmorel.homework.client.screens.search.SearchViewImpl;
import com.github.nmorel.homework.client.screens.unauthorized.UnauthorizedPresenter;
import com.github.nmorel.homework.client.screens.unauthorized.UnauthorizedPresenterImpl;
import com.github.nmorel.homework.client.screens.unauthorized.UnauthorizedView;
import com.github.nmorel.homework.client.screens.unauthorized.UnauthorizedViewImpl;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class HomeworkModule
    extends AbstractGinModule
{

    @Override
    protected void configure()
    {
        // MVP
        bind( AcceptsOneWidget.class ).to( SimplePanel.class ).in( Singleton.class );
        bind( EventBus.class ).to( SimpleEventBus.class ).in( Singleton.class );
        bind( PlaceHistoryMapper.class ).to( AppPlaceHistoryMapper.class ).in( Singleton.class );
        bind( AppActivityMapper.class ).in( Singleton.class );

        // Authenticated user
        bind( UserContainer.class ).in( Singleton.class );

        // Messages
        bind( Messages.class ).in( Singleton.class );

        // Main Controller
        bind( MainPresenter.class ).to( MainPresenterImpl.class ).in( Singleton.class );
        bind( MainView.class ).to( MainViewImpl.class ).in( Singleton.class );

        // Error view
        bind( ErrorActivity.class ).in( Singleton.class );

        bind( UnauthorizedPresenter.class ).to( UnauthorizedPresenterImpl.class ).in( Singleton.class );
        bind( UnauthorizedView.class ).to( UnauthorizedViewImpl.class );

        // Views are singleton
        bind( SearchView.class ).to( SearchViewImpl.class ).in( Singleton.class );
        bind( RepoView.class ).to( RepoViewImpl.class ).in( Singleton.class );
    }

    @Provides
    @Singleton
    public PlaceHistoryHandler provideHistoryHandler( PlaceController placeController,
                                                      PlaceHistoryMapper historyMapper, EventBus eventBus,
                                                      ActivityManager activityManager, ResourcesBundle resourcesBundle )
    {
        PlaceHistoryHandler historyHandler = new PlaceHistoryHandler( historyMapper );
        historyHandler.register( placeController, eventBus, TokenEnum.SEARCH.createNewPlace() );
        return historyHandler;
    }

    @Provides
    @Singleton
    public PlaceController providePlaceController( EventBus eventBus )
    {
        return new PlaceController( eventBus );
    }

    @Provides
    @Singleton
    public ActivityManager provideActivityManager( AppActivityMapper mapper, EventBus eventBus,
                                                   MainPresenter mainPresenter )
    {
        ActivityManager activityManager = new ActivityManager( mapper, eventBus );
        activityManager.setDisplay( mainPresenter.getView().getContainer() );
        return activityManager;
    }

    @Provides
    @Singleton
    public ResourcesBundle provideResourcesBundle()
    {
        ResourcesBundle bundle = GWT.create( ResourcesBundle.class );
        bundle.style().ensureInjected();
        return bundle;
    }

    @Provides
    public User provideUser( UserContainer userContainer )
    {
        return userContainer.getUser();
    }
}
