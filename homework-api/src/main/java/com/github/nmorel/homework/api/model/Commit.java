package com.github.nmorel.homework.api.model;

public class Commit
{
    private User committer;

    private String message;

    private String url;

    public User getCommitter()
    {
        return committer;
    }

    public String getMessage()
    {
        return message;
    }

    public String getUrl()
    {
        return url;
    }

}
