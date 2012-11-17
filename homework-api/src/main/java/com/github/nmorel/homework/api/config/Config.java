package com.github.nmorel.homework.api.config;

public class Config
{
    private String githubClientId;
    private String githubAuthorizeUrl;
    private String githubTokenUrl;
    private String githubApiBaseUrl;

    public String getGithubClientId()
    {
        return githubClientId;
    }

    public void setGithubClientId( String githubClientId )
    {
        this.githubClientId = githubClientId;
    }

    public String getGithubAuthorizeUrl()
    {
        return githubAuthorizeUrl;
    }

    public void setGithubAuthorizeUrl( String githubAuthorizeUrl )
    {
        this.githubAuthorizeUrl = githubAuthorizeUrl;
    }

    public String getGithubTokenUrl()
    {
        return githubTokenUrl;
    }

    public void setGithubTokenUrl( String githubTokenUrl )
    {
        this.githubTokenUrl = githubTokenUrl;
    }

    public String getGithubApiBaseUrl()
    {
        return githubApiBaseUrl;
    }

    public void setGithubApiBaseUrl( String githubApiBaseUrl )
    {
        this.githubApiBaseUrl = githubApiBaseUrl;
    }

}
