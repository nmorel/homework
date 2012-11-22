package com.github.nmorel.homework.api.config;

/**
 * Bean containing the configuration of the application
 * 
 * @author Nicolas Morel
 */
public class Config
{
    private String githubClientId;
    private String githubClientSecret;
    private String githubAuthorizeUrl;
    private String githubTokenUrl;
    private String githubApiBaseUrl;

    public Config()
    {
        // those informations are confidential and can't be present inside properties file
        githubClientId = System.getProperty( "github.client.id" );
        githubClientSecret = System.getProperty( "github.client.secret" );
    }

    /**
     * @return the github client id for this application
     */
    public String getGithubClientId()
    {
        return githubClientId;
    }

    /**
     * @return the github client secret for this application
     */
    public String getGithubClientSecret()
    {
        return githubClientSecret;
    }

    /**
     * @return the url to redirect user for authentification to github
     */
    public String getGithubAuthorizeUrl()
    {
        return githubAuthorizeUrl;
    }

    /**
     * @return the url to retrieve the user's token
     */
    public String getGithubTokenUrl()
    {
        return githubTokenUrl;
    }

    /**
     * @return the base url to the github api
     */
    public String getGithubApiBaseUrl()
    {
        return githubApiBaseUrl;
    }

}
