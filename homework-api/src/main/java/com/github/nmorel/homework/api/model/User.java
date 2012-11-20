package com.github.nmorel.homework.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a Github user
 * 
 * @author Nicolas Morel
 */
public class User
{
    private String login;

    private String name;

    private String email;

    @SerializedName( "avatar_url" )
    private String avatarUrl;

    public String getLogin()
    {
        return login;
    }

    public void setLogin( String login )
    {
        this.login = login;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail( String email )
    {
        this.email = email;
    }

    public String getAvatarUrl()
    {
        return avatarUrl;
    }

    public void setAvatarUrl( String avatarUrl )
    {
        this.avatarUrl = avatarUrl;
    }

}
