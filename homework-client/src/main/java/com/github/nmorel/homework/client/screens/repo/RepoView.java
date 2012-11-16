package com.github.nmorel.homework.client.screens.repo;

import com.github.nmorel.homework.client.ui.View;

public interface RepoView
    extends View
{
    interface Presenter
    {
    }

    void setPresenter( Presenter presenter );

}
