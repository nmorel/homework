package com.github.nmorel.homework.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the response to the token request
 * 
 * @author Nicolas Morel
 */
public class AccessToken
{
    @SerializedName( "access_token" )
    private String accessToken;

    @SerializedName( "token_type" )
    private String tokenType;

    public String getAccessToken()
    {
        return accessToken;
    }

    public String getTokenType()
    {
        return tokenType;
    }

}
