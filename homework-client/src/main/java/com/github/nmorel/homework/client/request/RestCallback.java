package com.github.nmorel.homework.client.request;

import com.github.nmorel.homework.client.utils.Alert;

public abstract class RestCallback<T>
{
    protected abstract void onSuccess( T result );

    protected void onError( Throwable throwable )
    {
        // Show an alert
        Alert.showError( throwable.getMessage() );
    }
}
