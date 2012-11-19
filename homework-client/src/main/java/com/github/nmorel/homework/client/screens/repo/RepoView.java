package com.github.nmorel.homework.client.screens.repo;

import com.github.nmorel.homework.client.model.FullCommit;
import com.github.nmorel.homework.client.ui.View;
import com.google.gwt.core.client.JsArray;

public interface RepoView
    extends View
{
    interface Presenter
    {
    }

    void setPresenter( Presenter presenter );

    void showResults( JsArray<FullCommit> commits );

}
