package com.github.nmorel.homework.client.model;

import com.google.gwt.core.client.JavaScriptObject;

public class DetailedRepository
    extends JavaScriptObject
{
    protected DetailedRepository()
    {
    }

    public final native User getOwner() /*-{
        return this.owner;
    }-*/;

    public final native String getName() /*-{
        return this.name;
    }-*/;

    public final native String getDescription() /*-{
        return this.description;
    }-*/;

    public final native String getLanguage() /*-{
        return this.language;
    }-*/;

    public final native double getSize() /*-{
        return this.size;
    }-*/;

    public final native int getNbForks() /*-{
        return this.forks;
    }-*/;

    public final native int getNbWatchers() /*-{
        return this.watchers;
    }-*/;

    public final native boolean isAFork() /*-{
        return this.fork;
    }-*/;

    public final native String getHomepage() /*-{
        return this.homepage;
    }-*/;
}
