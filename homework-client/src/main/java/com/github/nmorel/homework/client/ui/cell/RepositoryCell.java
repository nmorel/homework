package com.github.nmorel.homework.client.ui.cell;

import com.github.nmorel.homework.client.model.Repository;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiRenderer;

public class RepositoryCell
    extends AbstractCell<Repository>
{
    interface RepositoryUiRenderer
        extends UiRenderer
    {
        void render( SafeHtmlBuilder sb, Repository repo );
    }

    private static RepositoryUiRenderer renderer = GWT.create( RepositoryUiRenderer.class );

    @Override
    public void render( Context context, Repository value, SafeHtmlBuilder sb )
    {
        renderer.render( sb, value );
    }
}
