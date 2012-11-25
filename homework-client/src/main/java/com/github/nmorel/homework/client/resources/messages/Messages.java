package com.github.nmorel.homework.client.resources.messages;

/**
 * Interface to represent the messages used by this application
 */
public interface Messages
    extends com.google.gwt.i18n.client.Messages
{

    String alertErrorTitle();

    String headerLoginButton();

    String headerLoginButtonTooltip();

    String headerSearchPlaceholder();

    String headerTitle();

    String repoCollabImpactColCollaborators();

    String repoCollabImpactColNbCommitsAsAuthor();

    String repoCollabImpactColNbCommitsAsCommitter();

    String repoCollabImpactTab();

    String repoCollaborators();

    String repoCommitsDescription();

    String repoCommitsTab();

    String repoCommitsToolbarFirst();

    String repoCommitsToolbarLast();

    String repoCommitsToolbarNext();

    String repoCommitsToolbarPrev();

    String repoCommitsToolbarZoom();

    String repoBytes( @PluralCount int bytes );

    String repoForks( @PluralCount int forks );

    String repoStars( @PluralCount int stars );

    String searchButton();

    String searchLoading();

    String searchNoResult();

    String searchTitle();

    String unauthorizedDescription();

    String unauthorizedLogin();

    String unauthorizedSkip();

    String unauthorizedTitle();
}
