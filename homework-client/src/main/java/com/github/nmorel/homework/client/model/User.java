package com.github.nmorel.homework.client.model;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Represents a Github user
 * 
 * @author Nicolas Morel
 */
public class User
    extends JavaScriptObject
{
    protected User()
    {
    }

    public final native String getName()/*-{
        return this.name;
    }-*/;

    public final native String getAvatarUrl()/*-{
        return this.avatar_url;
    }-*/;

    public static final native User getUser()/*-{
        if (typeof ($wnd["userInfos"]) != "object") {
            return null;
        }
        return $wnd["userInfos"];
    }-*/;
}
