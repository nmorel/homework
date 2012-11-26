package com.github.nmorel.homework.api.model;

/**
 * Represents a repository
 * 
 * @author Nicolas Morel
 */
public class Repository
{
    private User owner;

    private String name;

    private String description;

    private boolean fork;

    private double forks;

    private double watchers;

    private double size;

    private String language;

    private String homepage;

    public User getOwner()
    {
        return owner;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public boolean isFork()
    {
        return fork;
    }

    public double getForks()
    {
        return forks;
    }

    public double getWatchers()
    {
        return watchers;
    }

    public double getSize()
    {
        return size;
    }

    public String getLanguage()
    {
        return language;
    }

    public String getHomepage()
    {
        return homepage;
    }

}
