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

    /**
     * @return the access token
     */
    public String getAccessToken()
    {
        return accessToken;
    }

    /**
     * @return the token type
     */
    public String getTokenType()
    {
        return tokenType;
    }

}
