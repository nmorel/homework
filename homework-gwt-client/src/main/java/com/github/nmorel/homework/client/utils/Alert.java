package com.github.nmorel.homework.client.utils;

import com.github.gwtbootstrap.client.ui.AlertBlock;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.github.nmorel.homework.client.Homework;
import com.github.nmorel.homework.client.ui.PopupAlertPanel;

public final class Alert
{
    private static PopupAlertPanel alertPanel;

    public static void showError( String message )
    {
        AlertBlock alert = new AlertBlock();
        alert.setAnimation( true );
        alert.setClose( true );
        alert.setHeading( Homework.ginjector.getMessages().alertErrorTitle() );
        alert.setText( message );
        alert.setType( AlertType.ERROR );
        alert.setWidth( "200px" );
        ensurePanel().addAlert( alert );
    }

    private static PopupAlertPanel ensurePanel()
    {
        if ( null == alertPanel )
        {
            alertPanel = new PopupAlertPanel();
        }
        return alertPanel;
    }
}
