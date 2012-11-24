package com.github.nmorel.homework.client.ui.repo;

import com.github.nmorel.homework.client.model.Commit;
import com.google.gwt.core.client.JsArray;

/**
 * Represents a tab in the repository view
 * 
 * @author Nicolas Morel
 */
public interface Tab
{
    /**
     * Called when the tab is selected
     */
    void onShow();

    /**
     * @return true if the tab has been initialized with datas
     */
    boolean isInitialized();

    /**
     * Sets the data to show
     * 
     * @param commits list of commits
     */
    void setData( final JsArray<Commit> commits );
}
