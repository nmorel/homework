package com.github.nmorel.homework.client.ui.repo.collaborators;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.github.nmorel.homework.client.model.Commit;
import com.github.nmorel.homework.client.model.Committer;
import com.github.nmorel.homework.client.ui.repo.AbstractTab;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.ChartArea;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.LegendPosition;
import com.google.gwt.visualization.client.visualizations.corechart.BarChart;
import com.google.gwt.visualization.client.visualizations.corechart.CoreChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;

/**
 * Collaborator's impact chart view
 * 
 * @author Nicolas Morel
 */
public class CollaboratorsImpactChart
    extends AbstractTab
{
    private static class Collaborator
        implements Comparable<Collaborator>
    {
        private String displayName;
        private List<Commit> asAuthor = Lists.newArrayList();
        private List<Commit> asCommitter = Lists.newArrayList();

        Collaborator( String displayName )
        {
            this.displayName = displayName;
        }

        public String getDisplayName()
        {
            return displayName;
        }

        public void addAsAuthor( Commit commit )
        {
            asAuthor.add( commit );
        }

        public void addAsCommitter( Commit commit )
        {
            asCommitter.add( commit );
        }

        public List<Commit> getAsAuthor()
        {
            return asAuthor;
        }

        public List<Commit> getAsCommitter()
        {
            return asCommitter;
        }

        @Override
        public int compareTo( Collaborator o )
        {
            return Ordering.<Integer> natural().reverse()
                .compare( asAuthor.size() + asCommitter.size(), o.getAsAuthor().size() + o.getAsCommitter().size() );
        }
    }

    interface Binder
        extends UiBinder<Widget, CollaboratorsImpactChart>
    {
    }

    private static final Logger logger = Logger.getLogger( CollaboratorsImpactChart.class.getName() );

    private static Binder uiBinder = GWT.create( Binder.class );

    @UiField
    SimplePanel graphContainer;

    private CoreChart chart;

    @Override
    public void init()
    {
        setWidget( uiBinder.createAndBindUi( this ) );
    }

    @Override
    protected void clearChart()
    {
        if ( null != chart )
        {
            chart.draw( DataTable.create() );
        }
    }

    @Override
    public void onShow()
    {
        // nothing to do
    }

    @Override
    protected void makeChart()
    {
        logger.fine( "Initializing Chart" );

        List<Collaborator> collaboratorsImpact = createCollaboratorsImpact( commits );

        // create a data table
        DataTable data = DataTable.create();
        data.addColumn( DataTable.ColumnType.STRING, getMessages().repoCollabImpactColCollaborators() );
        data.addColumn( DataTable.ColumnType.NUMBER, getMessages().repoCollabImpactColNbCommitsAsAuthor() );
        data.addColumn( DataTable.ColumnType.NUMBER, getMessages().repoCollabImpactColNbCommitsAsCommitter() );

        int i = 0;
        for ( Collaborator collaborator : collaboratorsImpact )
        {
            // create a row for the commit
            data.addRow();
            data.setValue( i, 0, collaborator.getDisplayName() );
            data.setValue( i, 1, collaborator.getAsAuthor().size() );
            data.setValue( i, 2, collaborator.getAsCommitter().size() );
            i++;
        }

        // create options
        Options options = Options.create();

        int totalWidth = 500;
        int decorationWidth = 100;
        int chartWidth = totalWidth - decorationWidth;

        int chartHeight = collaboratorsImpact.size() * 25;
        int topDecorationHeight = 50;
        int bottomDecorationHeight = 30;
        int totalHeight = chartHeight + topDecorationHeight + bottomDecorationHeight;

        options.setWidth( totalWidth );
        options.setHeight( totalHeight );
        options.setFontName( "Helvetica Neue,Helvetica,Arial,sans-serif" );
        options.setFontSize( 13 );
        options.setLegend( LegendPosition.TOP );

        ChartArea chartArea = ChartArea.create();
        chartArea.setTop( topDecorationHeight );
        chartArea.setLeft( decorationWidth );
        chartArea.setWidth( chartWidth );
        chartArea.setHeight( chartHeight );
        options.setChartArea( chartArea );

        // create the timeline, with data and options
        if ( null == chart )
        {
            chart = new BarChart( data, options );
            graphContainer.setWidget( chart );
        }
        else
        {
            chart.draw( data, options );
        }

        logger.fine( "Chart initialized" );
    }

    private List<Collaborator> createCollaboratorsImpact( JsArray<Commit> commits )
    {
        Map<String, Collaborator> collaboratorsImpact = Maps.newHashMap();
        for ( int i = 0; i < commits.length(); i++ )
        {
            Commit commit = commits.get( i );
            Committer author = commit.getAuthor();
            if ( null != author )
            {
                // it can happen that the login and avatar url is unknown. In this case, we use the email as key.
                String key = null == author.getLogin() ? author.getEmail() : author.getLogin();
                Collaborator collaborator = collaboratorsImpact.get( key );
                if ( null == collaborator )
                {
                    collaborator = new Collaborator( author.getName() );
                    collaboratorsImpact.put( key, collaborator );
                }
                collaborator.addAsAuthor( commit );
            }
            Committer committer = commit.getCommitter();
            if ( null != committer )
            {
                // it can happen that the login and avatar url is unknown. In this case, we use the email as key.
                String key = null == committer.getLogin() ? committer.getEmail() : committer.getLogin();
                Collaborator collaborator = collaboratorsImpact.get( key );
                if ( null == collaborator )
                {
                    collaborator = new Collaborator( committer.getName() );
                    collaboratorsImpact.put( key, collaborator );
                }
                collaborator.addAsCommitter( commit );
            }
        }

        List<Collaborator> result = Lists.newArrayList( collaboratorsImpact.values() );
        Collections.sort( result );
        return result;
    }
}
