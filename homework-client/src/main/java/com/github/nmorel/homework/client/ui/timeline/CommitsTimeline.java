package com.github.nmorel.homework.client.ui.timeline;

import java.util.Date;
import java.util.logging.Logger;

import com.chap.links.client.Timeline;
import com.chap.links.client.events.SelectHandler;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.nmorel.homework.client.model.Commit;
import com.github.nmorel.homework.client.model.Committer;
import com.github.nmorel.homework.client.resources.Constants;
import com.github.nmorel.homework.client.resources.TimelineBundle;
import com.github.nmorel.homework.client.ui.View;
import com.github.nmorel.homework.client.ui.VisualizationLoader;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayUtils;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.Selection;

/**
 * Commits Timeline view
 * 
 * @author Nicolas Morel
 */
public class CommitsTimeline
    extends SimpleLayoutPanel
    implements View
{
    private class CommitSelectHandler
        extends SelectHandler
    {
        @Override
        public void onSelect( SelectEvent event )
        {
            onChangeSelection();
        }
    }

    interface Binder
        extends UiBinder<Widget, CommitsTimeline>
    {
    }

    // TODO max-width and ellipsis
    interface CommitTemplate
        extends SafeHtmlTemplates
    {
        @SafeHtmlTemplates.Template( "<div><img src=\"{0}\" style=\"width:20px; height:20px; margin-right: 5px;\"/>{1}</div>" )
        SafeHtml commit( String avatarUrl, String message );

        @SafeHtmlTemplates.Template( "<div>{0}</div>" )
        SafeHtml commit( String message );
    }

    private static final Logger logger = Logger.getLogger( CommitsTimeline.class.getName() );

    private static Binder uiBinder = GWT.create( Binder.class );

    private static TimelineBundle timelineBundle = GWT.create( TimelineBundle.class );

    private static CommitTemplate template = GWT.create( CommitTemplate.class );

    @UiField( provided = true )
    DockLayoutPanel root;

    @UiField
    SimplePanel timelineContainer;

    @UiField
    Button first;

    @UiField
    Button prev;

    @UiField
    Button next;

    @UiField
    Button last;

    private Timeline currentTimeline;

    private JsArray<Commit> commits;

    @Override
    public void init()
    {
        // inject the timeline css
        timelineBundle.style().ensureInjected();

        // little trick to redraw automatically the timeline when the browser is resizing
        root = new DockLayoutPanel( Unit.PX ) {
            @Override
            public void onResize()
            {
                super.onResize();
                if ( null != currentTimeline )
                {
                    currentTimeline.redraw();
                }
            }
        };
        setWidget( uiBinder.createAndBindUi( this ) );
    }

    @Override
    public void clear()
    {
        currentTimeline = null;
        commits = null;
        timelineContainer.clear();
    }

    /**
     * Sets timeline data
     * 
     * @param commits
     */
    public void setData( final JsArray<Commit> commits )
    {
        this.commits = commits;
        if ( VisualizationLoader.isLoaded() )
        {
            makeTimeline();
        }
        else
        {
            logger.fine( "Visualization api isn't loaded yet, we wait before creating the timeline" );
            VisualizationLoader.addPendingCommand( new Command() {

                @Override
                public void execute()
                {
                    makeTimeline();
                }
            } );
        }
    }

    @SuppressWarnings( "deprecation" )
    private void makeTimeline()
    {
        logger.fine( "Initializing Timeline" );

        // create a data table
        DataTable data = DataTable.create();
        data.addColumn( DataTable.ColumnType.DATETIME, "startdate" );
        data.addColumn( DataTable.ColumnType.DATETIME, "enddate" );
        data.addColumn( DataTable.ColumnType.STRING, "content" );

        DateTimeFormat dtf = Constants.GITHUB_DATE_FORMAT;

        Date minCommitDate = null;
        Date maxCommitDate = null;

        for ( int i = 0; i < commits.length(); i++ )
        {
            Commit commit = commits.get( i );
            // We take the author as committer. Maybe add something later to show both.
            Committer committer = commit.getAuthor();

            Date commitDate = dtf.parse( committer.getDate() );
            if ( null == minCommitDate || commitDate.before( minCommitDate ) )
            {
                minCommitDate = commitDate;
            }
            if ( null == maxCommitDate || commitDate.after( maxCommitDate ) )
            {
                maxCommitDate = commitDate;
            }

            // create a row for the commit
            data.addRow();
            data.setValue( i, 0, commitDate );
            SafeHtml sh;
            if ( null == committer.getAvatarUrl() )
            {
                sh = template.commit( commit.getMessage() );
            }
            else
            {
                sh = template.commit( committer.getAvatarUrl(), commit.getMessage() );
            }
            data.setValue( i, 2, sh.asString() );
        }

        // create options
        Timeline.Options options = Timeline.Options.create();
        options.setShowNavigation( true );
        options.setWidth( "100%" );
        options.setHeight( "500px" );
        options.setStyle( Timeline.Options.STYLE.BOX );
        options.setBoxAlign( "left" );

        if ( null != minCommitDate )
        {
            minCommitDate.setDate( minCommitDate.getDate() - 7 );
            options.setMin( minCommitDate );
        }

        if ( null != maxCommitDate )
        {
            maxCommitDate.setDate( maxCommitDate.getDate() + 7 );
            options.setMax( maxCommitDate );
        }

        options.setEventMargin( 5 );

        // no need to go down 1 min interval
        options.setIntervalMin( 60000 );
        options.setShowCurrentTime( false );
        options.setEditable( false );

        // create the timeline, with data and options
        currentTimeline = new Timeline( data, options );
        currentTimeline.addSelectHandler( new CommitSelectHandler() );

        timelineContainer.setWidget( currentTimeline );

        currentTimeline.setVisibleChartRangeAuto();

        logger.fine( "Timeline initialized" );
    }

    /**
     * On click on the toolbar button 'All commits'
     * 
     * @param e event
     */
    @UiHandler( "auto" )
    void onClickAuto( ClickEvent e )
    {
        currentTimeline.setVisibleChartRangeAuto();
    }

    /**
     * On click on the toolbar button 'First commit'
     * 
     * @param e event
     */
    @UiHandler( "first" )
    void onClickFirst( ClickEvent e )
    {
        selectRow( 0 );
    }

    /**
     * On click on the toolbar button 'Previous commit'
     * 
     * @param e event
     */
    @UiHandler( "prev" )
    void onClickPrev( ClickEvent e )
    {
        Integer currentRow = getSelectedRow();
        if ( null == currentRow )
        {
            selectRow( 0 );
        }
        else
        {
            selectRow( currentRow - 1 );
        }
    }

    /**
     * On click on the toolbar button 'Next commit'
     * 
     * @param e event
     */
    @UiHandler( "next" )
    void onClickNext( ClickEvent e )
    {
        Integer currentRow = getSelectedRow();
        if ( null == currentRow )
        {
            selectRow( commits.length() - 1 );
        }
        else
        {
            selectRow( currentRow + 1 );
        }
    }

    /**
     * On click on the toolbar button 'Last commit'
     * 
     * @param e event
     */
    @UiHandler( "last" )
    void onClickLast( ClickEvent e )
    {
        selectRow( commits.length() - 1 );
    }

    /**
     * Select the specified row
     * 
     * @param row row to select
     */
    private void selectRow( int row )
    {
        currentTimeline.setSelections( JsArrayUtils.readOnlyJsArray( new Selection[] { Selection
            .createRowSelection( row ) } ) );
        onChangeSelection();
    }

    /**
     * @return the current selected row, null if none selected
     */
    private Integer getSelectedRow()
    {
        if ( currentTimeline.getSelections().length() == 0 )
        {
            return null;
        }
        else
        {
            return currentTimeline.getSelections().get( 0 ).getRow();
        }
    }

    /**
     * When the selection change, we update the toolbar buttons state and show the commit detail.
     */
    private void onChangeSelection()
    {
        Integer selectedRow = getSelectedRow();
        logger.fine( "Commit selected : " + selectedRow );

        // Enable/disable toolbar buttons
        boolean firstSelected = null != selectedRow && selectedRow == 0;
        boolean lastSelected = null != selectedRow && selectedRow == commits.length() - 1;
        first.setEnabled( !firstSelected );
        prev.setEnabled( !firstSelected );
        next.setEnabled( !lastSelected );
        last.setEnabled( !lastSelected );

        // TODO show a commit detail
    }
}
