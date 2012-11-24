package com.github.nmorel.homework.client.screens.repo;

import com.github.nmorel.homework.client.model.Commit;
import com.github.nmorel.homework.client.model.User;
import com.github.nmorel.homework.client.ui.AbstractView;
import com.github.nmorel.homework.client.ui.cell.CollaboratorCell;
import com.github.nmorel.homework.client.ui.repo.collaborators.CollaboratorsImpactChart;
import com.github.nmorel.homework.client.ui.repo.commits.CommitsTimeline;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.TabLayoutPanel;
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

    @UiField
    TabLayoutPanel tabPanel;

    @UiField( provided = true )
    CommitsTimeline commitsTimeline;

    @UiField( provided = true )
    CollaboratorsImpactChart collaboratorsImpactChart;

    private Presenter presenter;

    // we keep an instance of the commits to initialize the tab which isn't visible
    private JsArray<Commit> commits;

    @Inject
    public RepoViewImpl( CommitsTimeline commitsTimeline, CollaboratorsImpactChart collaboratorsImpactChart )
    {
        this.commitsTimeline = commitsTimeline;
        this.collaboratorsImpactChart = collaboratorsImpactChart;
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
        collaboratorsImpactChart.init();

        Widget widget = uiBinder.createAndBindUi( this );

        tabPanel.addSelectionHandler( new SelectionHandler<Integer>() {

            @Override
            public void onSelection( SelectionEvent<Integer> event )
            {
                presenter.onTabChange( event.getSelectedItem() );
            }
        } );

        return widget;
    }

    @Override
    public void showResults( final JsArray<Commit> commits )
    {
        this.commits = commits;

        if ( tabPanel.getSelectedIndex() == 0 )
        {
            commitsTimeline.setData( commits );
        }
        else
        {
            collaboratorsImpactChart.setData( commits );
        }
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
        commits = null;
        collaboratorsListProvider.getList().clear();
        commitsTimeline.clear();
        collaboratorsImpactChart.clear();
    }

    @Override
    public void selectTab( final int tab )
    {
        tabPanel.selectTab( tab, false );

        if ( null != commits )
        {
            Scheduler.get().scheduleDeferred( new ScheduledCommand() {

                @Override
                public void execute()
                {
                    if ( tabPanel.getSelectedIndex() == 0 )
                    {
                        // commits
                        commitsTimeline.onShow();
                        if ( !commitsTimeline.isInitialized() )
                        {
                            commitsTimeline.setData( commits );
                        }
                    }
                    else
                    {
                        // collabo
                        collaboratorsImpactChart.onShow();
                        if ( !collaboratorsImpactChart.isInitialized() )
                        {
                            collaboratorsImpactChart.setData( commits );
                        }
                    }
                }
            } );
        }
    }
}
