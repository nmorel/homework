package com.github.nmorel.homework.client.mvp;

import com.github.nmorel.homework.client.place.ErrorPlace;
import com.github.nmorel.homework.client.place.SearchPlace;
import com.github.nmorel.homework.client.place.TokenEnum;
import com.google.common.base.Objects;
import com.google.gwt.place.shared.Place;

public abstract class BasePlace
    extends Place
{
    public interface Visitor
    {
        void visitPlace( SearchPlace place );

        void visitPlace( ErrorPlace errorPlace );
    }

    public abstract void accept( Visitor visitor );

    public abstract TokenEnum getToken();

    public PlaceWithParameters hasParameters()
    {
        return null;
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "token", getToken().token() ).toString();
    }
}
