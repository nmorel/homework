package com.github.nmorel.homework.client.screens.main;

import com.github.nmorel.homework.client.place.SearchPlace;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

public class MainPresenterImpl
    implements MainPresenter
{
    private MainView view;

    @Inject
    private PlaceController placeController;

    @Inject
    public MainPresenterImpl( final MainView view )
    {
        this.view = view;
        this.view.setPresenter( this );
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
}
