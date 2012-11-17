package com.github.nmorel.homework.client.ui;

import com.github.nmorel.homework.client.resources.ResourcesBundle;
import com.github.nmorel.homework.client.resources.messages.Messages;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public abstract class AbstractView
    implements View
{
    @Inject
    private Messages messages;

    @Inject
    private ResourcesBundle resources;

    private Widget widget;

    @UiFactory
    public Messages getMessages()
    {
        return messages;
    }

    @UiFactory
    public ResourcesBundle getResources()
    {
        return resources;
    }

    @Override
    public Widget asWidget()
    {
        if ( null == widget )
        {
            init();
        }
        return widget;
    }

    @Override
    public void init()
    {
        if ( null == widget )
        {
            widget = initWidget();
        }
    }

    protected abstract Widget initWidget();

}
