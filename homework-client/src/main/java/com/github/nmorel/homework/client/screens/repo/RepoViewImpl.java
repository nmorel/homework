package com.github.nmorel.homework.client.screens.repo;

import com.github.nmorel.homework.client.model.Commit;
import com.github.nmorel.homework.client.model.User;
import com.github.nmorel.homework.client.ui.AbstractView;
import com.github.nmorel.homework.client.ui.cell.CollaboratorCell;
import com.github.nmorel.homework.client.ui.timeline.CommitsTimeline;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;

public class RepoViewImpl
    extends AbstractView
    implements RepoView
{
    private static Binder uiBinder = GWT.create( Binder.class );

    interface Binder
        extends UiBinder<Widget, RepoViewImpl>
    {
    }

    @UiField( provided = true )
    CellList<User> collaboratorsList;

    private ListDataProvider<User> collaboratorsListProvider;

    @UiField( provided = true )
    CommitsTimeline commitsTimeline;

    private Presenter presenter;

    @Inject
    public RepoViewImpl( CommitsTimeline commitsTimeline )
    {
        this.commitsTimeline = commitsTimeline;
    }

    @Override
    public void setPresenter( Presenter presenter )
    {
        this.presenter = presenter;
    }

    @Override
    protected Widget initWidget()
    {
        collaboratorsList = new CellList<User>( new CollaboratorCell() );
        // doesn't need pagination
        collaboratorsList.setPageSize( Integer.MAX_VALUE );
        collaboratorsListProvider = new ListDataProvider<User>();
        collaboratorsListProvider.addDataDisplay( collaboratorsList );

        commitsTimeline.init();

        return uiBinder.createAndBindUi( this );
    }

    @Override
    public void showResults( final JsArray<Commit> commits )
    {
        commitsTimeline.setData( commits );
    }

    @Override
    public void showCollaborators( final JsArray<User> collaborators )
    {
        collaboratorsListProvider.getList().clear();
        for ( int i = 0; i < collaborators.length(); i++ )
        {
            collaboratorsListProvider.getList().add( collaborators.get( i ) );
        }
    }

    @Override
    public void clear()
    {
        collaboratorsListProvider.getList().clear();
        commitsTimeline.clear();
    }
}
