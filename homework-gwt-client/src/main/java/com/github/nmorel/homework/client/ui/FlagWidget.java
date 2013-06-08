package com.github.nmorel.homework.client.ui;

import com.github.gwtbootstrap.client.ui.Tooltip;
import com.github.gwtbootstrap.client.ui.constants.Placement;
import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * A simple loading widget.
 * 
 * @author Nicolas Morel
 */
public class FlagWidget
    extends Composite
{
    interface FlagStyle
        extends CssResource
    {
        String disabled();

        String enabled();
    }

    interface FlagWidgetUiBinder
        extends UiBinder<Widget, FlagWidget>
    {
    }

    private static FlagWidgetUiBinder uiBinder = GWT.create( FlagWidgetUiBinder.class );

    @UiField
    FlagStyle style;

    @UiField( provided = true )
    InlineHTML container;

    @UiConstructor
    public FlagWidget( ImageResource flag, final String locale, final String tooltip )
    {
        this.container = new InlineHTML( AbstractImagePrototype.create( flag ).getSafeHtml() );
        this.container.setPixelSize( flag.getWidth(), flag.getHeight() );

        initWidget( uiBinder.createAndBindUi( this ) );

        String localeParam = Window.Location.getParameter( "locale" );
        if ( Strings.isNullOrEmpty( localeParam ) && Strings.isNullOrEmpty( locale ) )
        {
            // default locale
            addStyleName( style.disabled() );
        }
        else if ( !Strings.isNullOrEmpty( localeParam ) && localeParam.equals( locale ) )
        {
            // current locale, we disable it
            addStyleName( style.disabled() );
        }
        else
        {
            // not current locale, we enable it and add a tooltip
            Tooltip tp = new Tooltip();
            tp.setWidget( container );
            tp.setText( tooltip );
            tp.setPlacement( Placement.BOTTOM );tp.setAnimation( true );
            tp.reconfigure();

            addStyleName( style.enabled() );
            this.container.addClickHandler( new ClickHandler() {

                @Override
                public void onClick( ClickEvent event )
                {
                    UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
                    if ( Strings.isNullOrEmpty( locale ) )
                    {
                        // default locale
                        urlBuilder.removeParameter( "locale" );
                    }
                    else
                    {
                        // adding locale as parameter
                        urlBuilder.setParameter( "locale", locale );
                    }
                    Window.Location.assign( urlBuilder.buildString() );

                }
            } );
        }
    }
}
