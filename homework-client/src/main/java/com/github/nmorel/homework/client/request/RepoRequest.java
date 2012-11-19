package com.github.nmorel.homework.client.request;

import com.google.common.base.Preconditions;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;

public class RepoRequest
    extends BaseRequest
{
    public RepoRequest()
    {
        setMethod( RequestBuilder.GET );
        setPath( "repos/{owner}/{repo}" );
    }

    public void fire( String owner, String repo, RequestCallback callback )
    {
        Preconditions.checkNotNull( owner );
        Preconditions.checkNotNull( repo );

        addArgument( owner );
        addArgument( repo );
        setCallback( callback );
        fire();
    }
}
