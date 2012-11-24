package com.github.nmorel.homework.api.services;

import javax.annotation.Nullable;

import com.google.common.base.Optional;

/**
 * Service to manipulate github access token
 * 
 * @author Nicolas Morel
 */
public interface OAuthTokenService
{
    /**
     * Retrieve the token from the code given by github and associated to the current user
     * 
     * @param code code gave by github
     * @throws IllegalArgumentException thrown if the code is null or empty
     */
    void retrieveAndStoreToken( String code );

    /**
     * @return the token associated to the current user, never return null.
     */
    Optional<String> getToken();

    /**
     * @param userId id of the user
     * @return the token associated to the user, never return null.
     */
    Optional<String> getToken( @Nullable String userId );

    /**
     * Delete the token associated to the current user if he exists
     */
    void deleteToken();

    /**
     * Delete the token associated to the user
     * 
     * @param userId id of the user
     */
    void deleteToken( @Nullable String userId );
}