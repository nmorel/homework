package com.github.nmorel.homework.client.ui.cell;

import com.github.nmorel.homework.client.model.User;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiRenderer;

public class CollaboratorCell
    extends AbstractCell<User>
{
    interface CollaboratorUiRenderer
        extends UiRenderer
    {
        void render( SafeHtmlBuilder sb, User collaborator );
    }

    private static CollaboratorUiRenderer renderer = GWT.create( CollaboratorUiRenderer.class );

    @Override
    public void render( Context context, User value, SafeHtmlBuilder sb )
    {
        renderer.render( sb, value );
    }
}
