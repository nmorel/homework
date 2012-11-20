package com.github.nmorel.homework.api.model;

/**
 * Represents a commit
 * 
 * @author Nicolas Morel
 */
public class Commit
{
    private String sha;

    private String message;

    private Committer author;

    private Committer committer;

    public String getSha()
    {
        return sha;
    }

    public void setSha( String sha )
    {
        this.sha = sha;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage( String message )
    {
        this.message = message;
    }

    public Committer getAuthor()
    {
        return author;
    }

    public void setAuthor( Committer author )
    {
        this.author = author;
    }

    public Committer getCommitter()
    {
        return committer;
    }

    public void setCommitter( Committer committer )
    {
        this.committer = committer;
    }

}
