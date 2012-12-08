package com.github.nmorel.homework.client.ui.repo.commits;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.chap.links.client.Timeline;
import com.chap.links.client.Timeline.DateRange;
import com.chap.links.client.events.SelectHandler;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.nmorel.homework.client.model.Commit;
import com.github.nmorel.homework.client.model.Committer;
import com.github.nmorel.homework.client.resources.Constants;
import com.github.nmorel.homework.client.resources.TimelineBundle;
import com.github.nmorel.homework.client.ui.repo.AbstractTab;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
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
    extends AbstractTab
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

    // FIXME bug under chrome : a padding appears between image and text
    interface CommitTemplate
        extends SafeHtmlTemplates
    {
        @SafeHtmlTemplates.Template( "<div class=\"{2}\"><img src=\"{0}\" class=\"{3}\"/>{1}</div>" )
        SafeHtml commit( SafeUri avatarUrl, String message, String classNameDiv, String classNameAvatar );

        @SafeHtmlTemplates.Template( "<div class=\"{1}\">{0}</div>" )
        SafeHtml commit( String message, String className );
    }

    private static final Logger logger = Logger.getLogger( CommitsTimeline.class.getName() );

    private static Binder uiBinder = GWT.create( Binder.class );

    private static TimelineBundle timelineBundle = GWT.create( TimelineBundle.class );

    private static CommitTemplate template = GWT.create( CommitTemplate.class );

    private static final double ZOOM_FACTOR = 0.5;

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

    /**
     * We keep track of the selected row because we lost it when we switch tabs
     */
    private Integer lastSelectedRow;

    private Timeline timeline;

    private List<Date> commitsDate;

    @Override
    public void init()
    {
        // inject the timeline css
        timelineBundle.style().ensureInjected();

        commitsDate = new ArrayList<Date>();
        
        setWidget( uiBinder.createAndBindUi( this ) );
    }

    @Override
    protected void clearChart()
    {
        lastSelectedRow = null;
        commitsDate.clear();
        if ( null != timeline )
        {
            timeline.deleteAllItems();
        }
    }

    @Override
    public void onShow()
    {
        if ( null != timeline && null != commits )
        {
            Scheduler.get().scheduleDeferred( new ScheduledCommand() {

                @Override
                public void execute()
                {
                    timeline.redraw();
                    if ( null != lastSelectedRow )
                    {
                        selectRow( lastSelectedRow );
                    }
                }
            } );
        }
    }

    @SuppressWarnings( "deprecation" )
    @Override
    protected void makeChart()
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
            commitsDate.add( commitDate );
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
                sh = template.commit( commit.getMessage(), getResources().style().repoCommitTimelineDivNoAvatar() );
            }
            else
            {
                sh =
                    template.commit( committer.getAvatarSafeUri(), commit.getMessage(), getResources().style()
                        .repoCommitTimelineDivAvatar(), getResources().style().repoCommitTimelineAvatar() );
            }
            data.setValue( i, 2, sh.asString() );
        }

        // create options
        Timeline.Options options = Timeline.Options.create();
        options.setShowNavigation( true );
        options.setWidth( "100%" );
        options.setHeight( "100%" );
        options.setStyle( Timeline.Options.STYLE.BOX );

        if ( null != minCommitDate )
        {
            options.setMin( new Date( minCommitDate.getYear(), minCommitDate.getMonth(), minCommitDate.getDate() - 7 ) );
        }

        if ( null != maxCommitDate )
        {
            options.setMax( new Date( maxCommitDate.getYear(), maxCommitDate.getMonth(), maxCommitDate.getDate() + 7 ) );
        }

        options.setEventMargin( 5 );

        // no need to go down 1 min interval
        options.setIntervalMin( 60000 );
        options.setShowCurrentTime( false );
        options.setEditable( false );

        // create the timeline, with data and options
        if ( null == timeline )
        {
            timeline = new Timeline( data, options );
            timeline.addSelectHandler( new CommitSelectHandler() );
            timeline.setSize( "100%", "100%" );
            timelineContainer.setWidget( timeline );
        }
        else
        {
            timeline.draw( data, options );
        }

        timeline.setVisibleChartRangeAuto();

        onChangeSelection();

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
        timeline.setVisibleChartRangeAuto();
    }

    /**
     * On click on the toolbar button 'Zoom in'
     * 
     * @param e event
     */
    @UiHandler( "zoomIn" )
    void onClickZoomIn( ClickEvent e )
    {
        zoom( ZOOM_FACTOR );
    }

    /**
     * On click on the toolbar button 'Zoom out'
     * 
     * @param e event
     */
    @UiHandler( "zoomOut" )
    void onClickZoomOut( ClickEvent e )
    {
        zoom( -ZOOM_FACTOR );
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
            selectClosest( true );
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
            selectClosest( false );
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
        JsArray<Selection> selection = JavaScriptObject.createArray().cast();
        selection.set( 0, Selection.createRowSelection( row ) );
        timeline.setSelections( selection );
        onChangeSelection();
    }

    /**
     * @return the current selected row, null if none selected
     */
    private Integer getSelectedRow()
    {
        if ( timeline.getSelections().length() == 0 )
        {
            return null;
        }
        else
        {
            return timeline.getSelections().get( 0 ).getRow();
        }
    }

    /**
     * When the selection change, we update the toolbar buttons state and show the commit detail.
     */
    private void onChangeSelection()
    {
        Integer selectedRow = getSelectedRow();
        logger.fine( "Commit selected : " + selectedRow );

        lastSelectedRow = selectedRow;

        // Enable/disable toolbar buttons
        boolean firstSelected = null != selectedRow && selectedRow == 0;
        boolean lastSelected = null != selectedRow && selectedRow == commits.length() - 1;
        first.setEnabled( !firstSelected );
        prev.setEnabled( !firstSelected );
        next.setEnabled( !lastSelected );
        last.setEnabled( !lastSelected );

        // TODO show a commit detail
    }

    /**
     * Select the closest row to the middle
     * 
     * @param left
     */
    private void selectClosest( boolean left )
    {
        DateRange range = timeline.getVisibleChartRange();
        Date midDate =
            new Date( range.getStart().getTime() + ( ( range.getEnd().getTime() - range.getStart().getTime() ) / 2 ) );
        int row = commitsDate.indexOf( midDate );
        if ( row == -1 )
        {
            selectRow( findClosestRow( 0, commitsDate.size() - 1, midDate, left ) );
        }
        else
        {
            // for the rare case, the midDate is exactly a commit date
            selectRow( row );
        }
    }

    /**
     * Determines the closest row to the date
     * 
     * @param startIndex
     * @param endIndex
     * @param midDate
     * @param left
     * @return
     */
    private int findClosestRow( int startIndex, int endIndex, Date midDate, boolean left )
    {
        if ( startIndex == endIndex )
        {
            return startIndex;
        }

        int diff = endIndex - startIndex;
        if ( diff == 1 )
        {
            // we have two dates left
            Date firstDate = commitsDate.get( startIndex );
            if ( firstDate.after( midDate ) )
            {
                return startIndex;
            }
            Date secondDate = commitsDate.get( endIndex );
            if ( secondDate.before( midDate ) )
            {
                return endIndex;
            }

            if ( left )
            {
                return startIndex;
            }
            else
            {
                return endIndex;
            }
        }

        int index = ( startIndex + endIndex ) / 2;
        Date date = commitsDate.get( index );

        if ( date.before( midDate ) )
        {
            return findClosestRow( index, endIndex, midDate, left );
        }
        else
        {
            return findClosestRow( startIndex, index, midDate, left );
        }
    }

    /**
     * Zomm in or out depending of the zoomFactor
     * 
     * @param zoomFactor
     */
    private void zoom( double zoomFactor )
    {
        timeline.zoom( zoomFactor );
    }
}
