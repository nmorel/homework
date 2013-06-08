package com.github.nmorel.homework.client.model;


/**
 * Represents an author or committer of a commit
 * 
 * @author Nicolas Morel
 */
public class Committer
    extends User
{
    protected Committer()
    {
    }

    public final native String getDate()/*-{
        return this.date;
    }-*/;
}
