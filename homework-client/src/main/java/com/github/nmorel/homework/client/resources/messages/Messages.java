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

    String recentRepos();

    String repoCollabImpactColCollaborators();

    String repoCollabImpactColNbCommitsAsAuthor();

    String repoCollabImpactColNbCommitsAsCommitter();

    String repoCollabImpactTab();

    String repoCollaborators();

    String repoCommitsDescription();

    String repoCommitsTab();

    String repoCommitsToolbarAuto();

    String repoCommitsToolbarFirst();

    String repoCommitsToolbarLast();

    String repoCommitsToolbarNext();

    String repoCommitsToolbarPrev();

    String repoCommitsToolbarZoomIn();

    String repoCommitsToolbarZoomOut();

    String repoBytes( @PluralCount int bytes );

    String repoForks( @PluralCount int forks );

    String repoStars( @PluralCount int stars );

    String searchButton();

    String searchError();

    String searchLoading();

    String searchNoResult();

    String searchTitle();

    String switchLocaleEn();

    String switchLocaleFr();

    String unauthorizedDescription();

    String unauthorizedLogin();

    String unauthorizedSkip();

    String unauthorizedTitle();
}
