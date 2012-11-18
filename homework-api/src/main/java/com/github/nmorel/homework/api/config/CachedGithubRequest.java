package com.github.nmorel.homework.api.config;

/**
 * A container for cached github request. It contains the Last-Modified header in order to pass it to the next request.
 * If the request returns a 304 Not modified, we return the cached result.
 * 
 * @author Nicolas Morel
 * @param <T>
 */
public class CachedGithubRequest<T>
{
    private String lastModified;

    private T result;

    public CachedGithubRequest( String lastModified, T result )
    {
        super();
        this.lastModified = lastModified;
        this.result = result;
    }

    public String getLastModified()
    {
        return lastModified;
    }

    public T getResult()
    {
        return result;
    }

}
