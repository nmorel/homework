package com.github.nmorel.homework.client.utils;

import com.github.gwtbootstrap.client.ui.AlertBlock;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.github.gwtbootstrap.client.ui.event.CloseEvent;
import com.github.gwtbootstrap.client.ui.event.CloseHandler;
import com.github.nmorel.homework.client.Homework;
import com.google.gwt.user.client.ui.PopupPanel;

public final class Alert
{
    public static void showError( String message )
    {
        final PopupPanel popup = new PopupPanel( false, true );
        popup.setGlassEnabled( true );
        popup.setGlassStyleName( Homework.ginjector.getResources().style().alertGlassPanel() );

        AlertBlock alert = new AlertBlock();
        alert.setAnimation( true );
        alert.setClose( true );
        alert.setHeading( Homework.ginjector.getMessages().alertErrorTitle() );
        alert.setText( message );
        alert.setType( AlertType.ERROR );
        alert.setWidth( "250px" );
        alert.addCloseHandler( new CloseHandler() {

            @Override
            public void onClose( CloseEvent closeEvent )
            {
                popup.hide();
            }
        } );

        popup.add( alert );
        popup.center();
    }
}
