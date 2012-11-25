package com.github.nmorel.homework.client.ui;

import com.github.gwtbootstrap.client.ui.ProgressBar;
import com.google.gwt.core.client.GWT;
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

    private static WaitingWidgetUiBinder uiBinder = GWT.create( WaitingWidgetUiBinder.class );

    interface WaitingWidgetUiBinder
        extends UiBinder<Widget, LoadingWidget>
    {
    }

    @UiField
    ProgressBar progressBar;

    @UiConstructor
    public LoadingWidget( String loadingText )
    {
        initWidget( uiBinder.createAndBindUi( this ) );
        progressBar.setText( loadingText );
    }

}
