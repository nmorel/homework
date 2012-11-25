package com.github.nmorel.homework.client.request;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.nmorel.homework.client.gin.UserContainer;
import com.github.nmorel.homework.client.screens.unauthorized.UnauthorizedPresenter;
import com.github.nmorel.homework.client.utils.Alert;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.google.inject.Inject;
import com.google.inject.Provider;

public abstract class BaseRequest
{
    /**
     * A default callback used to handle the errors
     * 
     * @author Nicolas Morel
     */
    private class BaseCallback
        implements RequestCallback
    {
        @SuppressWarnings( "unchecked" )
        @Override
        public void onResponseReceived( Request request, Response response )
        {
            int statusCode = response.getStatusCode();
            if ( statusCode == 200 )
            {
                if ( logger.isLoggable( Level.FINE ) )
                {
                    logger.fine( "The request succeeded : " + BaseRequest.this );
                }
                if ( null != callback )
                {
                    if ( Strings.isNullOrEmpty( response.getText() ) )
                    {
                        callback.onSuccess( null );
                    }
                    else
                    {
                        callback.onSuccess( JsonUtils.safeEval( response.getText() ) );
                    }
                }
            }
            else if ( statusCode == 401 && null != userContainer.getUser() )
            {
                // we tried an authenticated request and received a 401 Unauthorized error.
                handleUnauthorizedResponse();
            }
            else
            {
                handleException( new StatusCodeException( statusCode, response.getText() ) );
            }
        }

        @Override
        public void onError( Request request, Throwable exception )
        {
            handleException( exception );
        }
    }

    private static final Logger logger = Logger.getLogger( BaseRequest.class.getName() );

    @Inject
    private Provider<RestRequestBuilder> requestBuilder;

    @Inject
    private UserContainer userContainer;

    @Inject
    private Provider<UnauthorizedPresenter> unauthorizedProvider;

    private Method method;

    private String path;

    private List<String> args = Lists.newArrayList();

    private Map<String, String> queryParams = Maps.newHashMap();

    private String data;

    @SuppressWarnings( "rawtypes" )
    private RestCallback callback;

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

    public void setCallback( RestCallback<?> callback )
    {
        this.callback = callback;
    }

    /**
     * Fires the request
     */
    void fire()
    {
        RestRequestBuilder builder = build();
        if ( logger.isLoggable( Level.FINE ) )
        {
            logger.fine( "Firing request : " + toString( builder ) );
        }
        try
        {
            builder.fire();
        }
        catch ( RequestException e )
        {
            handleException( e );
        }
    }

    protected RestRequestBuilder build()
    {
        return requestBuilder.get().build( method, path, args, queryParams ).withData( data )
            .withCallback( new BaseCallback() );
    }

    private void handleException( Throwable exception )
    {
        logger.log( Level.SEVERE, "The request failed : " + this, exception );

        if ( null == callback )
        {
            // No callback is defined, we just show an alert
            Alert.showError( exception.getMessage() );
        }
        else
        {
            // we delegate to the callback
            callback.onError( exception );
        }
    }

    protected void handleUnauthorizedResponse()
    {
        if ( logger.isLoggable( Level.FINE ) )
        {
            logger.fine( "The request failed because the user's token is no longer valid : " + this );
        }
        unauthorizedProvider.get().show( RetryRequest.of( this ) );
    }

    private String toString( RestRequestBuilder builder )
    {
        return Objects.toStringHelper( this ).add( "url", builder.getUrl() ).add( "data", data ).toString();
    }

    @Override
    public String toString()
    {
        return toString( build() );
    }
}
