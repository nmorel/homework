package com.github.nmorel.homework.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

public interface ResourcesBundle
    extends ClientBundle
{
    public interface Style
        extends CssResource
    {
        String rootContainer();
        
        String headerContainer();

        String headerTitle();
        
        String headerSearchContainer();
        
        String headerSearchKeyword();
        
        String headerSearchButton();
        
        String headerLoginContainer();
        
        String headerFlagsContainer();

        String recentReposContainer();

        String recentReposTitleContainer();

        String recentReposTitle();

        String recentReposScroll();

        String contentContainer();
        
        String contentInnerContainer();
        
        String searchFiltersContainer();

        String searchContainer();

        String searchFormContainer();
        
        String searchResultContainer();

        String searchNoResult();

        String searchError();

        String repoCollaboratorsContainer();

        String repoCollaboratorsTitleContainer();

        String repoCollaboratorsTitle();

        String repoCollaboratorCell();

        String repoCollaboratorAvatar();

        String repoContainer();

        String repoInnerContainer();

        String repoTitleContainer();

        String repoCommitTimelineDivAvatar();

        String repoCommitTimelineDivNoAvatar();

        String repoCommitTimelineAvatar();

        String errorLabel();

        String alertGlassPanel();
    }

    @Source( "images/flagEn.png" )
    ImageResource flagEn();

    @Source( "images/flagFr.png" )
    ImageResource flagFr();

    @Source( { "style.css" } )
    Style style();
}
