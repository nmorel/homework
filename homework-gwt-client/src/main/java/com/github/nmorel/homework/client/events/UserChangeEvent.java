package com.github.nmorel.homework.client.events;

import com.github.nmorel.homework.client.model.User;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Represents a user change event.
 */
public class UserChangeEvent
    extends GwtEvent<UserChangeEvent.Handler>
{

    /**
     * Handler interface for {@link UserChangeEvent} events.
     */
    public static interface Handler
        extends EventHandler
    {

        /**
         * Called when a {@link UserChangeEvent} is fired.
         * 
         * @param event the {@link UserChangeEvent} that was fired
         */
        void onUserChange( UserChangeEvent event );
    }

    /**
     * Handler type.
     */
    private static Type<UserChangeEvent.Handler> TYPE;

    /**
     * Gets the type associated with this event.
     * 
     * @return returns the handler type
     */
    public static Type<UserChangeEvent.Handler> getType()
    {
        if ( TYPE == null )
        {
            TYPE = new Type<UserChangeEvent.Handler>();
        }
        return TYPE;
    }

    private User user;

    /**
     * Creates a selection change event.
     */
    public UserChangeEvent( User user )
    {
        this.user = user;
    }

    public User getUser()
    {
        return user;
    }

    @Override
    public final Type<UserChangeEvent.Handler> getAssociatedType()
    {
        return TYPE;
    }

    @Override
    protected void dispatch( UserChangeEvent.Handler handler )
    {
        handler.onUserChange( this );
    }
}
