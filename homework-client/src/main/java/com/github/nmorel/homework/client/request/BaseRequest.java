package com.github.nmorel.homework.client.request;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.http.client.RequestCallback;
import com.google.inject.Inject;
import com.google.inject.Provider;

public abstract class BaseRequest
{

    @Inject
    private Provider<RestRequestBuilder> requestBuilder;

    private Method method;

    private String path;

    private List<String> args = Lists.newArrayList();

    private Map<String, String> queryParams = Maps.newHashMap();

    private String data;

    private RequestCallback callback;

    protected void setMethod( Method method )
    {
        this.method = method;
    }

    protected void setPath( String path )
    {
        this.path = path;
    }

    protected void addArgument( String arg )
    {
        args.add( arg );
    }

    protected void addQueryParameter( String param, String value )
    {
        queryParams.put( param, value );
    }

    protected void setData( String data )
    {
        this.data = data;
    }

    public void setCallback( RequestCallback callback )
    {
        this.callback = callback;
    }

    protected void fire()
    {
        RestRequestBuilder builder =
            requestBuilder.get().build( method, path, args, queryParams ).withData( data ).withCallback( callback );
        completeRequest( builder ).fire();
    }

    protected RestRequestBuilder completeRequest( RestRequestBuilder builder )
    {
        return builder;
    }
}
