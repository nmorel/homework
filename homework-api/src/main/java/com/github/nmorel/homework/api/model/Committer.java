package com.github.nmorel.homework.api.model;

/**
 * Represents an author or committer of a commit
 * 
 * @author Nicolas Morel
 */
public class Committer
    extends User
{
    /**
     * Date of the commit
     */
    private String date;

    public String getDate()
    {
        return date;
    }

    public void setDate( String date )
    {
        this.date = date;
    }

}
