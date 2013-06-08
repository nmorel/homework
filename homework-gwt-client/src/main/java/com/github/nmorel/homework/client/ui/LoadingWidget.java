package com.github.nmorel.homework.client.ui;

import com.github.gwtbootstrap.client.ui.ProgressBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * A simple loading widget.
 * 
 * @author Nicolas Morel
 */
public class LoadingWidget
    extends Composite
{
    interface LoadingStyle
        extends CssResource
    {
        String progress();

        String progressCenter();
    }

    interface LoadingWidgetUiBinder
        extends UiBinder<Widget, LoadingWidget>
    {
    }

    private static LoadingWidgetUiBinder uiBinder = GWT.create( LoadingWidgetUiBinder.class );

    @UiField
    LoadingStyle style;

    @UiField
    DivElement progressContainer;

    @UiField
    ProgressBar progressBar;

    @UiConstructor
    public LoadingWidget( String loadingText )
    {
        initWidget( uiBinder.createAndBindUi( this ) );
        progressBar.setText( loadingText );
    }

    public void setCenter( boolean center )
    {
        if ( center )
        {
            progressContainer.setClassName( style.progressCenter() );
        }
        else
        {
            progressContainer.setClassName( style.progress() );
        }
    }

}
