package com.github.nmorel.homework.client.screens.unauthorized;

import com.github.gwtbootstrap.client.ui.base.HasVisibility;

public interface UnauthorizedView
    extends HasVisibility
{
    interface HasUiHandlers
    {
        void onLogin();

        void onSkip();

        void onClose();
    }

    void setHasUiHandlers( HasUiHandlers hasUiHandlers );
}
