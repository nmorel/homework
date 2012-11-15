package com.github.nmorel.homework.client.mvp;

import com.github.nmorel.homework.client.mvp.BasePlace.Visitor;
import com.github.nmorel.homework.client.place.ErrorPlace;
import com.github.nmorel.homework.client.place.SearchPlace;

public class NoOpVisitor
    implements Visitor
{

    @Override
    public void visitPlace( SearchPlace place )
    {
    }

    @Override
    public void visitPlace( ErrorPlace errorPlace )
    {
    }

}
