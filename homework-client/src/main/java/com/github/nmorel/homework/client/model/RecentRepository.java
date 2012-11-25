package com.github.nmorel.homework.client.model;

import com.google.gwt.core.client.JavaScriptObject;

public class RecentRepository
    extends JavaScriptObject
{    
    protected RecentRepository()
    {
    }

    public final native String getOwner() /*-{
        return this.owner;
    }-*/;

    public final native void setOwner(String owner) /*-{
        this.owner = owner;
    }-*/;

    public final native String getName() /*-{
        return this.name;
    }-*/;

    public final native void setName(String name) /*-{
        this.name = name;
    }-*/;
}
