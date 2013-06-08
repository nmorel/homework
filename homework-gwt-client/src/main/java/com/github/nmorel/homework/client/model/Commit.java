package com.github.nmorel.homework.client.model;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Represents a commit
 * 
 * @author Nicolas Morel
 */
public class Commit
    extends JavaScriptObject
{
    protected Commit()
    {
    }

    public final native String getMessage()/*-{
        return this.message;
    }-*/;

    public final native Committer getAuthor()/*-{
        return this.author;
    }-*/;

    public final native Committer getCommitter()/*-{
        return this.committer;
    }-*/;
}
