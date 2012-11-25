package com.github.nmorel.homework.client.gin;

import com.github.nmorel.homework.client.model.User;

public class UserContainer
{
    private User user;

    public UserContainer()
    {
        user = getNativeUser();
    }

    public User getUser()
    {
        return user;
    }

    public void setUser( User user )
    {
        this.user = user;
    }

    private final native User getNativeUser()/*-{
        if (typeof ($wnd["userInfos"]) != "object") {
            return null;
        }
        return $wnd["userInfos"];
    }-*/;

}
