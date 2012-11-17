package com.github.nmorel.homework.client.screens.main;

import com.github.nmorel.homework.client.model.User;
import com.github.nmorel.homework.client.place.SearchPlace;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
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
        // we add the hash as a parameter so the server can retrieve it
        String hash = Window.Location.getHash();
        String baseUrl;
        if ( null == hash || hash.length() <= 1 )
        {
            baseUrl = GWT.isProdMode() ? "/authorize" : "/authorize?gwt.codesvr=127.0.0.1:9997";
        }
        else
        {
            // we remove the # and encode the rest
            String encodedHash = URL.encodeQueryString( hash.substring( 1 ) );
            baseUrl =
                ( GWT.isProdMode() ? "/authorize?hash=" : "/authorize?gwt.codesvr=127.0.0.1:9997&hash=" ) + encodedHash;
        }
        Window.Location.assign( baseUrl );
    }
}
