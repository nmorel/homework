package com.github.nmorel.homework.client.place;

import java.util.Map;

import com.github.nmorel.homework.client.mvp.AppPlaceHistoryMapper;
import com.github.nmorel.homework.client.mvp.BasePlace;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public enum TokenEnum
{
    SEARCH( "search" )
    {
        @Override
        public BasePlace createNewPlace()
        {
            return new SearchPlace();
        }
    },
    ERROR( "error" )
    {
        @Override
        public BasePlace createNewPlace()
        {
            return new ErrorPlace( "Oops, something went wrong!" );
        }
    };

    private String token;

    private TokenEnum( String token )
    {
        this.token = token;
    }

    public String token()
    {
        return token;
    }

    private static Map<String, TokenEnum> mapTokenToEnum = initMap();

    private static Map<String, TokenEnum> initMap()
    {
        Builder<String, TokenEnum> builder = ImmutableMap.builder();
        for ( TokenEnum tokenEnum : values() )
        {
            builder.put( tokenEnum.token(), tokenEnum );
        }
        return builder.build();
    }

    public abstract BasePlace createNewPlace();

    public static TokenEnum fromToken( String token )
    {
        if ( null == token )
        {
            return null;
        }
        int indexOfSeparator = token.indexOf( AppPlaceHistoryMapper.SEPARATOR_TOKEN_PARAMETERS );
        if ( indexOfSeparator == -1 )
        {
            return mapTokenToEnum.get( token );
        }
        else
        {
            return mapTokenToEnum.get( token.substring( 0, indexOfSeparator ) );
        }
    }
}
