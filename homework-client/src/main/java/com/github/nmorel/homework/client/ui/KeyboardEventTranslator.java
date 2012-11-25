package com.github.nmorel.homework.client.ui;

import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.DefaultSelectionEventManager.EventTranslator;
import com.google.gwt.view.client.DefaultSelectionEventManager.SelectAction;

/**
 * Custom event translator allowing the selection with the enter key. By default, only space key works.
 * 
 * @author Nicolas Morel
 * @param <T>
 */
public class KeyboardEventTranslator<T>
    implements EventTranslator<T>
{

    @Override
    public boolean clearCurrentSelection( CellPreviewEvent<T> event )
    {
        return false;
    }

    @Override
    public SelectAction translateSelectionEvent( CellPreviewEvent<T> event )
    {
        NativeEvent nativeEvent = event.getNativeEvent();
        String type = nativeEvent.getType();
        if ( BrowserEvents.CLICK.equals( type ) )
        {
            if ( nativeEvent.getCtrlKey() || nativeEvent.getMetaKey() )
            {
                // Toggle selection on ctrl+click.
                return SelectAction.TOGGLE;
            }
            else
            {
                // Select on click.
                return SelectAction.SELECT;
            }
        }
        else if ( BrowserEvents.KEYUP.equals( type ) )
        {
            // Toggle selection on space and enter.
            int keyCode = nativeEvent.getKeyCode();
            if ( keyCode == 32 || keyCode == KeyCodes.KEY_ENTER )
            {
                return SelectAction.SELECT;
            }
        }
        return SelectAction.IGNORE;
    }

}
