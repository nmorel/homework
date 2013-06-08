package com.github.nmorel.homework.client.gin;

import com.github.nmorel.homework.client.resources.ResourcesBundle;
import com.github.nmorel.homework.client.resources.messages.Messages;
import com.github.nmorel.homework.client.screens.main.MainPresenter;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceHistoryHandler;

@GinModules( { HomeworkModule.class } )
public interface HomeworkGinjector
    extends Ginjector
{
    PlaceHistoryHandler getPlaceHistoryHandler();

    MainPresenter getMainPresenter();

    Messages getMessages();

    ResourcesBundle getResources();

}
