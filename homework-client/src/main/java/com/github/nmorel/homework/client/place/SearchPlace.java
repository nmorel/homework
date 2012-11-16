package com.github.nmorel.homework.client.place;

import java.util.Map;

import com.github.nmorel.homework.client.mvp.BasePlace;
import com.github.nmorel.homework.client.mvp.PlaceWithParameters;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;

public class SearchPlace
    extends BasePlace
    implements PlaceWithParameters
{
    private static final String PARAM_KEYWORD = "keyword";
    
    private String keyword;

    public SearchPlace()
    {
    }

    public SearchPlace( String keyword )
    {
        this.keyword = keyword;
    }

    public String getKeyword()
    {
        return keyword;
    }

    @Override
    public void accept( Visitor visitor )
    {
        visitor.visitPlace( this );
    }

    @Override
    public TokenEnum getToken()
    {
        return TokenEnum.SEARCH;
    }

    @Override
    public PlaceWithParameters hasParameters()
    {
        return this;
    }

    @Override
    public Map<String, String> getParameters()
    {
        if ( Strings.isNullOrEmpty( keyword ) )
        {
            return null;
        }
        else
        {
            return ImmutableMap.of( PARAM_KEYWORD, keyword );
        }
    }

    @Override
    public void setParameters( Map<String, String> parameters )
        throws MissingParametersException
    {
        keyword = parameters.get( PARAM_KEYWORD );
    }

}
