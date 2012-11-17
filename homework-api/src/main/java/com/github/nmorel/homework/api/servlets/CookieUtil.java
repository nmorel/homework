package com.github.nmorel.homework.api.servlets;

import javax.servlet.http.Cookie;

import com.google.common.base.Objects;

public final class CookieUtil
{
    public static final String USER_ID = "user_id";

    public static String getFirstCookieValue( Cookie[] cookies, String name )
    {
        if ( null != cookies )
        {
            for ( Cookie cookie : cookies )
            {
                if ( Objects.equal( name, cookie.getName() ) )
                {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
