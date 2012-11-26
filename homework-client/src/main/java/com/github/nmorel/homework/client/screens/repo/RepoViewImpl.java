package com.github.nmorel.homework.client.screens.repo;

import java.util.logging.Logger;

import com.github.gwtbootstrap.client.ui.Heading;
import com.github.nmorel.homework.client.model.Commit;
import com.github.nmorel.homework.client.model.DetailedRepository;
import com.github.nmorel.homework.client.model.User;
import com.github.nmorel.homework.client.ui.AbstractView;
import com.github.nmorel.homework.client.ui.LoadingWidget;
import com.github.nmorel.homework.client.ui.State;
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
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HeaderPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;

public class RepoViewImpl
    extends AbstractView
    implements RepoView
{
    interface ListResources
        extends CellList.Resources
    {
        /**
         * The styles used in this widget.
         */
        @Source( "collaboratorsList.css" )
        ListStyle cellListStyle();
    }

    interface ListStyle
        extends CellList.Style
    {

    }

    interface Binder
        extends UiBinder<Widget, RepoViewImpl>
    {
    }

    private static final Logger logger = Logger.getLogger( RepoViewImpl.class.getName() );

    private static final ListResources listResources = GWT.create( ListResources.class );

    private static Binder uiBinder = GWT.create( Binder.class );

    @UiField
    HeaderPanel collaboratorsPanel;

    @UiField( provided = true )
    CellList<User> collaboratorsList;

    private ListDataProvider<User> collaboratorsListProvider;

    @UiField
    LayoutPanel container;

    @UiField
    LoadingWidget loading;

    @UiField
    HeaderPanel content;

    private boolean titleLoading;
    private boolean commitsLoading;
    
    @UiField
    HTMLPanel titleContainer;

    @UiField
    Heading title;

    @UiField
    Label description;

    @UiField
    Anchor homepage;

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
        collaboratorsList = new CellList<User>( new CollaboratorCell(), listResources );
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
    public void showRepositoryInformations( DetailedRepository repository )
    {
        title.setText( repository.getOwner().getLogin() + " / " + repository.getName() );

        description.setVisible( null != repository.getDescription() );
        description.setText( repository.getDescription() );

        homepage.setVisible( null != repository.getHomepage() );
        homepage.setHref( repository.getHomepage() );
        homepage.setText( repository.getHomepage() );
        // TODO show the rest of the informations
    }

    @Override
    public void showCommits( final JsArray<Commit> commits )
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
        title.setText( null );
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

    @Override
    public void setStateTitle( State state )
    {
        switch ( state )
        {
            case DEFAULT:
            case LOADING:
                titleLoading = true;
                updateLoadingState();
                break;
            case LOADED:
                titleLoading = false;
                titleContainer.setVisible( true );
                updateLoadingState();
                break;
            case ERROR:
                titleLoading = false;
                titleContainer.setVisible( false );
                updateLoadingState();
                break;
        }
    }

    @Override
    public void setStateCommits( State state )
    {
        switch ( state )
        {
            case DEFAULT:
            case LOADING:
                commitsLoading = true;
                updateLoadingState();
                break;
            case LOADED:
                commitsLoading = false;
                tabPanel.setVisible( true );
                updateLoadingState();
                break;
            case ERROR:
                commitsLoading = false;
                tabPanel.setVisible( false );
                updateLoadingState();
                break;
        }
    }

    @Override
    public void setStateCollaborators( State state )
    {
        switch ( state )
        {
            case DEFAULT:
            case LOADING:
            case ERROR:
                collaboratorsPanel.setVisible( false );
                break;
            case LOADED:
                if ( collaboratorsListProvider.getList().isEmpty() )
                {
                    collaboratorsPanel.setVisible( false );
                }
                else
                {
                    collaboratorsPanel.setVisible( true );
                    collaboratorsPanel.onResize();
                }
                break;
        }
    }

    private void updateLoadingState()
    {
        boolean isLoading = titleLoading || commitsLoading;
        container.setWidgetVisible( loading, isLoading );
        container.setWidgetVisible( content, !isLoading );
        container.onResize();
    }
}
