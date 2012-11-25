package com.github.nmorel.homework.client.ui.cell;

import com.github.nmorel.homework.client.model.Repository;
import com.github.nmorel.homework.client.mvp.AppPlaceHistoryMapper;
import com.github.nmorel.homework.client.place.RepoPlace;
import com.github.nmorel.homework.client.resources.messages.Messages;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.inject.Inject;

public class RepositoryCell
    extends AbstractCell<Repository>
{
    interface Template
        extends SafeHtmlTemplates
    {
        @SafeHtmlTemplates.Template( "<a href='#{0}' style='font-size: 14px; font-weight: bold;'>{1} / {2}</a>" )
        SafeHtml title( String href, String owner, String name );

        @SafeHtmlTemplates.Template( "<span style='font-size: 12px; color: #999; position: relative; top: -1px; cursor: text;'> ({0})</span>" )
        SafeHtml language( String language );

        @SafeHtmlTemplates.Template( "<div><span style='cursor: text;'>{0}</span></div>" )
        SafeHtml description( String description );

        @SafeHtmlTemplates.Template( "<div style='font-size: 10px; color: #555'><span style='cursor: text;'>{0} | {1} | {2}</span></div>" )
        SafeHtml details( String size, String forks, String stars );
    }

    private static final String[] sizeStrings = new String[] { " B", " KB", " MB", " GB" };

    private static Template template = GWT.create( Template.class );

    private static NumberFormat sizeFormat = NumberFormat.getFormat( "#0.#" );

    @Inject
    private Messages messages;

    @Inject
    private AppPlaceHistoryMapper historyMapper;

    @Override
    public void render( Context context, Repository repo, SafeHtmlBuilder sb )
    {
        sb.appendHtmlConstant( "<div>" );

        // Title
        sb.appendHtmlConstant( "<div>" );
        String href = historyMapper.getToken( new RepoPlace( repo.getOwner(), repo.getName() ) );
        sb.append( template.title( href, repo.getOwner(), repo.getName() ) );
        if ( null != repo.getLanguage() )
        {
            sb.append( template.language( repo.getLanguage() ) );
        }
        sb.appendHtmlConstant( "</div>" );

        // Description
        if ( null != repo.getDescription() )
        {
            sb.append( template.description( repo.getDescription() ) );
        }

        // Details
        String sizeFormatted;
        // Looking at Github, the size we get is size in bytes / 1000 and not 1024.
        Double realSize = new Double( repo.getSize() ) * 1000d;
        int currentIndex = 0;
        while ( realSize > 1024d && currentIndex + 1 < sizeStrings.length )
        {
            currentIndex++;
            realSize = realSize / 1024d;
        }

        if ( currentIndex == 0 )
        {
            // this is bytes
            sizeFormatted = messages.repoBytes( realSize.intValue() );
        }
        else
        {
            sizeFormatted = sizeFormat.format( realSize ) + sizeStrings[currentIndex];
        }

        String forks = messages.repoForks( repo.getNbForks() );
        String stars = messages.repoStars( repo.getNbFollowers() );
        sb.append( template.details( sizeFormatted, forks, stars ) );

        sb.appendHtmlConstant( "</div>" );
    }
}
