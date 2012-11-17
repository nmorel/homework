package com.github.nmorel.homework.client.screens.main;

public interface MainPresenter
{

    MainView getView();

    void onSearch( String keyword );

    void onLogin();

}
