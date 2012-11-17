package com.github.nmorel.homework.client.screens.main;

import com.github.nmorel.homework.client.model.User;
import com.github.nmorel.homework.client.ui.View;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public interface MainView
    extends View
{

    public interface Presenter
    {
    }

    void setPresenter( MainPresenter presenter );

    AcceptsOneWidget getContainer();

    void setUser( User user );
}
