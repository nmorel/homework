package com.github.nmorel.homework.client.screens.repo;

import com.github.nmorel.homework.client.model.Commit;
import com.github.nmorel.homework.client.model.User;
import com.github.nmorel.homework.client.ui.View;
import com.google.gwt.core.client.JsArray;

public interface RepoView
    extends View
{
    interface Presenter
    {
        void onTabChange( int tab );
    }

    void setPresenter( Presenter presenter );

    void showResults( JsArray<Commit> commits );

    void showCollaborators( JsArray<User> collaborators );

    void selectTab( int tab );

}
