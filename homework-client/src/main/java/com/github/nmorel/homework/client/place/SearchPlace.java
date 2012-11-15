package com.github.nmorel.homework.client.place;

import com.github.nmorel.homework.client.mvp.BasePlace;

public class SearchPlace
    extends BasePlace
{

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

}
