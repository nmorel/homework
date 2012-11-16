package com.github.nmorel.homework.client.screens.search;

import com.github.nmorel.homework.client.ui.View;

public interface SearchView
    extends View
{
    interface Presenter
    {

        void onSearch( String query );

    }

    void setPresenter( Presenter presenter );

}
