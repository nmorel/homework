package com.github.nmorel.homework.api.config;

public class Config
{
    private String githubClientId;
    private String githubClientSecret;
    private String githubAuthorizeUrl;
    private String githubTokenUrl;
    private String githubApiBaseUrl;

    public Config()
    {
        githubClientId = System.getProperty( "github.client.id" );
        githubClientSecret = System.getProperty( "github.client.secret" );
    }

    public String getGithubClientId()
    {
        return githubClientId;
    }

    public String getGithubClientSecret()
    {
        return githubClientSecret;
    }

    public String getGithubAuthorizeUrl()
    {
        return githubAuthorizeUrl;
    }

    public String getGithubTokenUrl()
    {
        return githubTokenUrl;
    }

    public String getGithubApiBaseUrl()
    {
        return githubApiBaseUrl;
    }

}
