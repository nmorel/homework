package com.github.nmorel.homework.client.mvp;

import java.util.Map;

import com.github.nmorel.homework.client.mvp.PlaceWithParameters.MissingParametersException;
import com.github.nmorel.homework.client.place.ErrorPlace;
import com.github.nmorel.homework.client.place.RepoPlace;
import com.github.nmorel.homework.client.place.SearchPlace;
import com.github.nmorel.homework.client.place.TokenEnum;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.gwt.place.shared.Place;

public abstract class BasePlace
    extends Place
{
    public interface Visitor
    {

        void visitPlace( ErrorPlace errorPlace );

        void visitPlace( SearchPlace place );

        void visitPlace( RepoPlace place );
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

    protected void checkMissingParameters( Map<String, String> parameters, String... mandatoryParameters )
        throws MissingParametersException
    {
        Builder<String> builder = null;
        for ( String mandatoryParameter : mandatoryParameters )
        {
            if ( !parameters.containsKey( mandatoryParameter ) )
            {
                if ( null == builder )
                {
                    builder = ImmutableList.<String> builder();
                }
                builder.add( mandatoryParameter );
            }
        }
        if ( null != builder )
        {
            throw new MissingParametersException( builder.build() );
        }
    }
}
