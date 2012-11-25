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

        String recentReposContainer();

        String recentReposTitleContainer();

        String recentReposTitle();

        String contentContainer();
        
        String searchContainer();
        
        String searchFormContainer();
        
        String repoContainer();

        String repoCollaboratorsContainer();

        String repoCollaboratorsTitleContainer();

        String repoCollaboratorsTitle();

        String repoCollaboratorsScroll();

        String repoCollaboratorCell();

        String repoCollaboratorAvatar();
        
        String repoCommitTimelineDivAvatar();
        
        String repoCommitTimelineDivNoAvatar();
        
        String repoCommitTimelineAvatar();

        String errorLabel();
        
        String alertGlassPanel();
    }

    @Source( { "style.css" } )
    Style style();
}
