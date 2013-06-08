package com.github.nmorel.homework.client.request;

/**
 * A wrapper to have access to the method {@link BaseRequest#fire}
 * 
 * @author Nicolas Morel
 */
public class RetryRequest
{
    static RetryRequest of( BaseRequest request )
    {
        return new RetryRequest( request );
    }

    private final BaseRequest request;

    private RetryRequest( BaseRequest request )
    {
        this.request = request;
    }

    public void retry()
    {
        request.fire();
    }

    @Override
    public int hashCode()
    {
        return request.hashCode();
    }

    @Override
    public boolean equals( Object obj )
    {
        return request.equals( obj );
    }
}
