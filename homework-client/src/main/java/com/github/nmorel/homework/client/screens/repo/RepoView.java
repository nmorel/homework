package com.github.nmorel.homework.client.screens.repo;

import com.github.nmorel.homework.client.model.Commit;
import com.github.nmorel.homework.client.model.DetailedRepository;
import com.github.nmorel.homework.client.model.User;
import com.github.nmorel.homework.client.ui.State;
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

    void showRepositoryInformations( DetailedRepository repository );

    void showCommits( JsArray<Commit> commits );

    void showCollaborators( JsArray<User> collaborators );

    void selectTab( int tab );

    void setStateTitle( State state );

    void setStateCommits( State state );

    void setStateCollaborators( State state );

}
