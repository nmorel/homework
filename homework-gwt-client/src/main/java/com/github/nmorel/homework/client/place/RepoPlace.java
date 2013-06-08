package com.github.nmorel.homework.client.place;

import java.util.Map;

import com.github.nmorel.homework.client.mvp.BasePlace;
import com.github.nmorel.homework.client.mvp.PlaceWithParameters;
import com.google.common.collect.ImmutableMap;

public class RepoPlace
    extends BasePlace
    implements PlaceWithParameters
{
    private static final String PARAM_NAME = "name";
    private static final String PARAM_OWNER = "owner";
    private static final String PARAM_TAB = "tab";

    private String owner;
    private String name;
    private int tab;
    private boolean reload = true;

    RepoPlace()
    {
    }

    public RepoPlace( String owner, String name )
    {
        this( owner, name, 0 );
    }

    public RepoPlace( String owner, String name, int tab )
    {
        this( owner, name, tab, true );
    }

    public RepoPlace( String owner, String name, int tab, boolean reload )
    {
        assert null != owner && null != name : "owner and name can't be null";
        this.owner = owner;
        this.name = name;
        this.tab = tab;
        this.reload = reload;
    }

    public String getOwner()
    {
        return owner;
    }

    public String getName()
    {
        return name;
    }

    public int getTab()
    {
        return tab;
    }

    public boolean isReload()
    {
        return reload;
    }

    @Override
    public void accept( Visitor visitor )
    {
        visitor.visitPlace( this );
    }

    @Override
    public TokenEnum getToken()
    {
        return TokenEnum.REPO;
    }

    @Override
    public PlaceWithParameters hasParameters()
    {
        return this;
    }

    @Override
    public Map<String, String> getParameters()
    {
        return ImmutableMap.of( PARAM_OWNER, owner, PARAM_NAME, name, PARAM_TAB, Integer.toString( tab ) );
    }

    @Override
    public void setParameters( Map<String, String> parameters )
        throws MissingParametersException
    {
        checkMissingParameters( parameters, PARAM_OWNER, PARAM_NAME );
        owner = parameters.get( PARAM_OWNER );
        name = parameters.get( PARAM_NAME );
        if ( parameters.containsKey( PARAM_TAB ) )
        {
            try
            {
                tab = Integer.parseInt( parameters.get( PARAM_TAB ) );
            }
            catch ( NumberFormatException e )
            {
            }
        }
    }
}
