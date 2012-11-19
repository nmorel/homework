package com.github.nmorel.homework.client.model;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Represents a Github user
 * 
 * @author Nicolas Morel
 */
public class Commit
    extends JavaScriptObject
{
    protected Commit()
    {
    }

    public final native User getCommitter()/*-{
        return this.committer;
    }-*/;

    public final native String getMessage()/*-{
        return this.message;
    }-*/;

    public final native String getUrl()/*-{
        return this.url;
    }-*/;
}
