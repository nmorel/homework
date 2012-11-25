package com.github.nmorel.homework.client.screens.search;

import com.github.nmorel.homework.client.model.Repository;
import com.github.nmorel.homework.client.ui.State;
import com.github.nmorel.homework.client.ui.View;
import com.google.gwt.core.client.JsArray;

public interface SearchView
    extends View
{
    interface Presenter
    {

        void onSearch( String keyword );

        void onSelectionRepository( Repository repo );

    }

    void setPresenter( Presenter presenter );

    void showResults( JsArray<Repository> repos );

    void setKeyword( String keyword );

    void setState( State state );

}
