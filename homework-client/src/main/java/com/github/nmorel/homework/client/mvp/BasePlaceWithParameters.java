package com.github.nmorel.homework.client.mvp;


public abstract class BasePlaceWithParameters
    extends BasePlace
    implements PlaceWithParameters
{
    @Override
    public PlaceWithParameters hasParameters()
    {
        return this;
    }
}
