package com.github.nmorel.homework.client.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;

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

    public final native String getLogin()/*-{
        return this.login;
    }-*/;

    public final native String getName()/*-{
        return this.name;
    }-*/;

    public final native String getEmail()/*-{
        return this.email;
    }-*/;

    public final native String getAvatarUrl()/*-{
        return this.avatar_url;
    }-*/;

    public final SafeUri getAvatarSafeUri()
    {
        return UriUtils.fromTrustedString( getAvatarUrl() );
    }

    public final SafeUri getUserSafeUri()
    {
        return UriUtils.fromTrustedString( "https://github.com/" + getLogin() );
    }
}
