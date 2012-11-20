package com.github.nmorel.homework.client.screens.repo;

import com.chap.links.client.Timeline;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.nmorel.homework.client.model.FullCommit;
import com.github.nmorel.homework.client.model.User;
import com.github.nmorel.homework.client.ui.AbstractView;
import com.github.nmorel.homework.client.ui.cell.CollaboratorCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;

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
    SimplePanel graphContainer;

    private Presenter presenter;

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
        return uiBinder.createAndBindUi( this );
    }

    @Override
    public void showResults( final JsArray<FullCommit> commits )
    {
        // Create a callback to be called when the visualization API
        // has been loaded.
        Runnable onLoadCallback = new Runnable() {
            public void run()
            {
                // create a data table
                DataTable data = DataTable.create();
                data.addColumn( DataTable.ColumnType.DATETIME, "startdate" );
                data.addColumn( DataTable.ColumnType.DATETIME, "enddate" );
                data.addColumn( DataTable.ColumnType.STRING, "content" );

                for ( int i = 0; i < commits.length(); i++ )
                {
                    FullCommit commit = commits.get( i );
                    // fill the table with some data
                    data.addRow();
                    data.setValue( i, 0, commit.getCommit().getCommitter().getCommitDate() );
                    StringBuilder render = new StringBuilder();
                    render.append( "<div><img src=\"" ).append( commit.getCommitter().getAvatarUrl() )
                        .append( "\" style=\"width:20px; height:20px\"/>" );
                    render.append( "<span>" ).append( commit.getCommit().getMessage() ).append( "</span></div>" );
                    data.setValue( i, 2, render.toString() );
                }

                // DateTimeFormat dtf = DateTimeFormat.getFormat( PredefinedFormat.ISO_8601 );
                // create options
                Timeline.Options options = Timeline.Options.create();
                options.setShowNavigation( true );
                options.setWidth( "100%" );
                options.setHeight( "500px" );
                options.setStyle( Timeline.Options.STYLE.BOX );
                // options.setMin( dtf.parse( "2010-08-20" ) );
                // options.setMax( dtf.parse( "2010-09-10" ) );
                options.setEditable( false );

                // create the timeline, with data and options
                Timeline timeline = new Timeline( data, options );

                graphContainer.setWidget( timeline );
            }
        };

        // Load the visualization api, passing the onLoadCallback to be called
        // when loading is done.
        VisualizationUtils.loadVisualizationApi( onLoadCallback );
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
}
