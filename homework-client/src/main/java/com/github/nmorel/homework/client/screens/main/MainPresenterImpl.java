package com.github.nmorel.homework.client.screens.main;

import com.google.inject.Inject;

public class MainPresenterImpl
    implements MainPresenter
{
    private MainView view;

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
}
