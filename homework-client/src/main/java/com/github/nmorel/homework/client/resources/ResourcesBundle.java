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
        String headerContainer();

        String headerInnerContainer();

        String headerTitle();

        String recentReposContainer();

        String recentReposTitleContainer();

        String recentReposTitle();

        String recentReposScroll();

        String contentContainer();

        String searchContainer();

        String searchFormContainer();

        String searchNoResult();

        String searchError();

        String repoContainer();

        String repoCollaboratorsContainer();

        String repoCollaboratorsTitleContainer();

        String repoCollaboratorsTitle();

        String repoCollaboratorsScroll();

        String repoCollaboratorCell();

        String repoCollaboratorAvatar();

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
