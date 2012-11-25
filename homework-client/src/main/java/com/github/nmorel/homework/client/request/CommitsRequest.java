package com.github.nmorel.homework.client.request;

import com.google.common.base.Preconditions;
import com.google.gwt.http.client.RequestBuilder;

/**
 * Request that list the 100 last commits on the given repository
 * 
 * @author Nicolas Morel
 */
public class CommitsRequest
    extends BaseRequest
{
    public CommitsRequest()
    {
        setMethod( RequestBuilder.GET );
        setPath( "repos/{owner}/{repo}/commits" );
    }

    public void fire( String owner, String repo, RestCallback<?> callback )
    {
        Preconditions.checkNotNull( owner );
        Preconditions.checkNotNull( repo );

        addArgument( owner );
        addArgument( repo );
        setCallback( callback );
        fire();
    }
}
