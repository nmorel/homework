package com.github.nmorel.homework.client.request;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;

public class SearchRequest
    extends BaseRequest
{
    public SearchRequest()
    {
        setMethod( RequestBuilder.GET );
        setPath( "repos" );
    }

    public void fire( String query, RequestCallback callback )
    {
        addQueryParameter( "query", query );
        setCallback( callback );
        fire();
    }
}
