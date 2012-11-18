package com.github.nmorel.homework.api.services;

import com.github.nmorel.homework.api.model.User;
import com.google.common.base.Optional;

/**
 * 
 * @author Nicolas Morel
 *
 */
public interface UserService
{
    /**
     * @return informations about the authenticated user, never return null.
     */
    Optional<User> getAuthenticatedUserInformations();
}
