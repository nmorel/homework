package com.github.nmorel.homework.client.screens.unauthorized;

import com.github.nmorel.homework.client.request.RetryRequest;

/**
 * Handle the request made to the server with a token no longer valid
 * 
 * @author Nicolas Morel
 */
public interface UnauthorizedPresenter
{
    /**
     * Show the popup
     * 
     * @param failedRequest
     */
    void show( RetryRequest failedRequest );
}
