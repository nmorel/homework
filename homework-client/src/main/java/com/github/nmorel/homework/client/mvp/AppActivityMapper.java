package com.github.nmorel.homework.client.mvp;

import com.github.nmorel.homework.client.place.ErrorPlace;
import com.github.nmorel.homework.client.place.SearchPlace;
import com.github.nmorel.homework.client.screens.error.ErrorActivity;
import com.github.nmorel.homework.client.screens.search.SearchActivity;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author Nicolas Morel
 */
public class AppActivityMapper
    implements ActivityMapper
{
    private class ActivityMapperVisitor
        implements BasePlace.Visitor
    {

        private Activity activity;

        public Activity getActivity()
        {
            return activity;
        }

        @Override
        public void visitPlace( ErrorPlace place )
        {
            this.activity = errorActivityProvider.get().withPlace( place );
        }

        @Override
        public void visitPlace( SearchPlace place )
        {
            this.activity = searchActivityProvider.get().withPlace( place );
        }

    }
    
    @Inject
    private Provider<ErrorActivity> errorActivityProvider;
    
    @Inject
    private Provider<SearchActivity> searchActivityProvider;

    @Override
    public Activity getActivity( Place place )
    {
        assert place instanceof BasePlace : "Only BasePlace are managed";
        ActivityMapperVisitor visitor = new ActivityMapperVisitor();
        ( (BasePlace) place ).accept( visitor );
        return visitor.getActivity();
    }
}
