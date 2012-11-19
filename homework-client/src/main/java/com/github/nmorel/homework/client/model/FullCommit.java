package com.github.nmorel.homework.client.model;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Represents a Github user
 * 
 * @author Nicolas Morel
 */
public class FullCommit
    extends JavaScriptObject
{
    protected FullCommit()
    {
    }

    public final native User getCommitter()/*-{
        return this.committer;
    }-*/;

    public final native Commit getCommit()/*-{
        return this.commit;
    }-*/;
}
