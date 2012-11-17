package com.github.nmorel.homework.api.services;

import com.google.common.base.Optional;

/**
 * Service to manipulate github access token
 * 
 * @author Nicolas Morel
 */
public interface OAuthTokenService
{
    /**
     * Retrieve the token from the code given by github and associated to the given user
     * 
     * @param userId id of the user
     * @param code code gave by github
     */
    void retrieveAndStoreToken( String userId, String code );

    /**
     * @param userId id of the user
     * @return the token associated to the user, null if none exists
     */
    Optional<String> getToken( String userId );

    /**
     * Delete the token associated to the user
     * 
     * @param userId id of the user
     */
    void deleteToken( String userId );
}