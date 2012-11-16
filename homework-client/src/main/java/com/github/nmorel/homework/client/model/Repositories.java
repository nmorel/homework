package com.github.nmorel.homework.client.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class Repositories
    extends JavaScriptObject
{
    protected Repositories()
    {
    }

    public final native JsArray<Repository> getRepositories() /*-{
        return this.repositories;
    }-*/;

}
