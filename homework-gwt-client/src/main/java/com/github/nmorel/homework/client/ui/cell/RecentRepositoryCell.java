package com.github.nmorel.homework.client.ui.cell;

import com.github.nmorel.homework.client.model.RecentRepository;
import com.github.nmorel.homework.client.mvp.AppPlaceHistoryMapper;
import com.github.nmorel.homework.client.place.RepoPlace;
import com.github.nmorel.homework.client.resources.messages.Messages;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.inject.Inject;

public class RecentRepositoryCell
    extends AbstractCell<RecentRepository>
{
    interface Template
        extends SafeHtmlTemplates
    {
        @SafeHtmlTemplates.Template( "<div style='overflow: hidden; text-overflow: ellipsis; white-space: nowrap;' title='{1} / {2}'><a href='#{0}' style='font-size: 13px;'>{1} / {2}</a></div>" )
        SafeHtml title( String href, String owner, String name );
    }

    private static Template template = GWT.create( Template.class );

    @Inject
    private Messages messages;

    @Inject
    private AppPlaceHistoryMapper historyMapper;

    @Override
    public void render( Context context, RecentRepository repo, SafeHtmlBuilder sb )
    {
        // Title
        String href = historyMapper.getToken( new RepoPlace( repo.getOwner(), repo.getName() ) );
        sb.append( template.title( href, repo.getOwner(), repo.getName() ) );
    }
}
