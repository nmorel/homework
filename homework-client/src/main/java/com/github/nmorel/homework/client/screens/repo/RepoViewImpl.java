package com.github.nmorel.homework.client.screens.repo;

import com.github.nmorel.homework.client.ui.AbstractView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class RepoViewImpl
    extends AbstractView
    implements RepoView
{
    private static Binder uiBinder = GWT.create( Binder.class );

    interface Binder
        extends UiBinder<Widget, RepoViewImpl>
    {
    }

    private Presenter presenter;

    @Override
    public void setPresenter( Presenter presenter )
    {
        this.presenter = presenter;
    }

    @Override
    protected Widget initWidget()
    {
        return uiBinder.createAndBindUi( this );
    }

}
