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

    @SerializedName( "avatar_url" )
    private String avatarUrl;

    /**
     * Date of a commit
     */
    private String date;

    public String getLogin()
    {
        return login;
    }

    public String getName()
    {
        return name;
    }

    public String getAvatarUrl()
    {
        return avatarUrl;
    }

    public String getDate()
    {
        return date;
    }
}
