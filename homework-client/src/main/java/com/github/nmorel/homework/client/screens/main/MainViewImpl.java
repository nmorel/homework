package com.github.nmorel.homework.client.screens.main;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class MainViewImpl
    extends Composite
    implements MainView
{

    private static Binder uiBinder = GWT.create( Binder.class );

    interface Binder
        extends UiBinder<Widget, MainViewImpl>
    {
    }

    private MainPresenter presenter;

    @UiField
    SimplePanel container;

    @Inject
    public MainViewImpl()
    {
        initWidget( uiBinder.createAndBindUi( this ) );
    }

    @Override
    public void setPresenter( MainPresenter presenter )
    {
        this.presenter = presenter;
    }

    @Override
    public AcceptsOneWidget getContainer()
    {
        return container;
    }
}
