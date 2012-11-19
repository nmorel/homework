package com.github.nmorel.homework.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface ResourcesBundle
    extends ClientBundle
{
    public interface Style
        extends CssResource
    {
        String headerContainer();
        
        String headerInnerContainer();
        
        String headerTitle();

        String westContainer();

        String contentContainer();

        String errorLabel();
    }

    @Source( { "style.css" } )
    Style style();
}
