package com.github.nmorel.homework.client.screens.main;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

public interface MainView
    extends IsWidget
{

    public interface Presenter
    {
    }

    void setPresenter( MainPresenter presenter );

    AcceptsOneWidget getContainer();
}
