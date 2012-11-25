package com.github.nmorel.homework.client.request;

import com.google.gwt.http.client.RequestBuilder;

/**
 * Request that search the repository corresponding to the given keyword
 * 
 * @author Nicolas Morel
 */
public class SearchRequest
    extends BaseRequest
{
    public SearchRequest()
    {
        setMethod( RequestBuilder.GET );
        setPath( "repos/search" );
    }

    public void fire( String query, RestCallback<?> callback )
    {
        addQueryParameter( "keyword", query );
        setCallback( callback );
        fire();
    }
}
