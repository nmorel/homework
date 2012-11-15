package com.github.nmorel.homework.client.place;

import com.github.nmorel.homework.client.mvp.BasePlace;

public class ErrorPlace
    extends BasePlace
{
    private String message;

    public ErrorPlace( String message )
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage( String message )
    {
        this.message = message;
    }

    @Override
    public void accept( Visitor visitor )
    {
        visitor.visitPlace( this );
    }

    @Override
    public TokenEnum getToken()
    {
        return TokenEnum.ERROR;
    }

}
