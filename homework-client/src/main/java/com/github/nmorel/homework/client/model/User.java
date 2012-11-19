package com.github.nmorel.homework.client.model;

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.DateTimeFormat;

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

    public final Date getCommitDate()
    {
        return DateTimeFormat.getFormat( "yyyy-MM-ddTHH:mm:ssZ" ).parse( getDate() );
    }

    private final native String getDate()/*-{
        return this.date;
    }-*/;

    public static final native User getUser()/*-{
        if (typeof ($wnd["userInfos"]) != "object") {
            return null;
        }
        return $wnd["userInfos"];
    }-*/;
}
