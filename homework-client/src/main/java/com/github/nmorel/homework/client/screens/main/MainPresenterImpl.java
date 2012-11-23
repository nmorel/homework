package com.github.nmorel.homework.client.screens.main;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.nmorel.homework.client.model.User;
import com.github.nmorel.homework.client.place.SearchPlace;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;

public class MainPresenterImpl
    implements MainPresenter
{
    private MainView view;

    @Inject
    private PlaceController placeController;

    @Inject
    public MainPresenterImpl( final MainView view, final User user )
    {
        this.view = view;
        this.view.init();
        this.view.setPresenter( this );
        this.view.setUser( user );
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
        UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
        urlBuilder.setHash( null );

        // add any current parameters
        Map<String, List<String>> parameters = Window.Location.getParameterMap();
        for ( Entry<String, List<String>> parameter : parameters.entrySet() )
        {
            for ( String value : parameter.getValue() )
            {
                urlBuilder.setParameter( parameter.getKey(), value );
            }
        }

        // we add the hash as a parameter so the server can retrieve it
        String hash = Window.Location.getHash();
        if ( null != hash )
        {
            if ( hash.startsWith( "#" ) )
            {
                hash = hash.substring( 1 );
            }
            // we remove the #
            urlBuilder.setParameter( "hash", hash );
        }

        String hostPageBaseUrl = GWT.getHostPageBaseURL();
        String url = urlBuilder.buildString();
        // we change the url to match the servlet. This is useful when a context path exists for the server. If we use
        // the setPath of the builder, we lose the context path.
        if ( url.startsWith( hostPageBaseUrl ) )
        {
            url = url.replaceFirst( hostPageBaseUrl, hostPageBaseUrl + "authorize" );
        }
        else if ( hostPageBaseUrl.endsWith( "/" ) )
        {
            hostPageBaseUrl = hostPageBaseUrl.substring( 0, hostPageBaseUrl.length() - 1 );
            url = url.replaceFirst( hostPageBaseUrl, hostPageBaseUrl + "/authorize" );
        }
        Window.Location.assign( url );
    }
}
