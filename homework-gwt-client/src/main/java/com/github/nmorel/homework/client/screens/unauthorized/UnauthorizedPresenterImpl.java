package com.github.nmorel.homework.client.screens.unauthorized;

import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import com.github.nmorel.homework.client.events.UserChangeEvent;
import com.github.nmorel.homework.client.gin.UserContainer;
import com.github.nmorel.homework.client.request.RetryRequest;
import com.github.nmorel.homework.client.screens.unauthorized.UnauthorizedView.HasUiHandlers;
import com.github.nmorel.homework.client.utils.LoginRedirection;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;

public class UnauthorizedPresenterImpl
    implements UnauthorizedPresenter, HasUiHandlers
{
    private static final Logger logger = Logger.getLogger( UnauthorizedPresenterImpl.class.getName() );

    @Inject
    private Provider<UnauthorizedView> viewProvider;

    @Inject
    private UserContainer userContainer;

    @Inject
    private EventBus eventBus;

    private final Set<RetryRequest> currentFailedRequest = Sets.newHashSet();

    private UnauthorizedView currentView;

    @Override
    public void show( RetryRequest failedRequest )
    {
        logger.fine( "Show unauthorized popup" );
        currentFailedRequest.add( failedRequest );
        if ( null == currentView )
        {
            currentView = viewProvider.get();
            currentView.setHasUiHandlers( this );
            currentView.show();
        }
    }

    @Override
    public void onLogin()
    {
        logger.fine( "User choosed to login" );
        LoginRedirection.redirectToGithubAuthorizationPage();
    }

    @Override
    public void onSkip()
    {
        // we just hide the view, the real operation will be made in the onClose method.
        currentView.hide();
    }

    @Override
    public void onClose()
    {
        currentView = null;

        // The user wants to continue without authenticating.
        logger.fine( "User prefer continue without login" );

        // We replay the requests
        Iterator<RetryRequest> iterator = currentFailedRequest.iterator();
        while ( iterator.hasNext() )
        {
            iterator.next().retry();
            iterator.remove();
        }

        // we delete the user in container and notify about it
        userContainer.setUser( null );
        eventBus.fireEvent( new UserChangeEvent( null ) );
    }

}
