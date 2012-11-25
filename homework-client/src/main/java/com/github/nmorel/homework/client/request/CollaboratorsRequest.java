package com.github.nmorel.homework.client.request;

import com.google.common.base.Preconditions;
import com.google.gwt.http.client.RequestBuilder;

/**
 * Request that list all the collaborators of the given repository
 * 
 * @author Nicolas Morel
 */
public class CollaboratorsRequest
    extends BaseRequest
{
    public CollaboratorsRequest()
    {
        setMethod( RequestBuilder.GET );
        setPath( "repos/{owner}/{repo}/collaborators" );
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
