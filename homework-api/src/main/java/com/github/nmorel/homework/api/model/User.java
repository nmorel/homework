package com.github.nmorel.homework.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a Github user
 * 
 * @author Nicolas Morel
 */
public class User
{
    private String name;

    @SerializedName( "avatar_url" )
    private String avatarUrl;

    public String getName()
    {
        return name;
    }

    public String getAvatarUrl()
    {
        return avatarUrl;
    }
}
