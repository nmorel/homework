package com.github.nmorel.homework.client.gin;

import com.github.nmorel.homework.client.screens.main.MainPresenter;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.web.bindery.event.shared.EventBus;

@GinModules({ HomeworkModule.class })
public interface HomeworkGinjector
    extends Ginjector
{
    PlaceHistoryHandler getPlaceHistoryHandler();

    EventBus getEventBus();

    PlaceController getPlaceController();
    
    MainPresenter getMainPresenter();
}
