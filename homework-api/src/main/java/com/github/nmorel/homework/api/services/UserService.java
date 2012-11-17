package com.github.nmorel.homework.api.services;

import com.github.nmorel.homework.api.model.User;
import com.google.common.base.Optional;

public interface UserService
{
    /**
     * @return information about the authenticated user
     */
    Optional<User> getAuthenticatedUser();
}
