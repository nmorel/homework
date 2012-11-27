package com.github.nmorel.homework.client;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.github.nmorel.homework.AbstractIT;
import com.google.mockwebserver.Dispatcher;
import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.RecordedRequest;

public class SeleniumIT
    extends AbstractIT
{
    private WebDriver driver;

    @Before
    public void init()
    {
        // Create a new instance of the Firefox driver
        // Notice that the remainder of the code relies on the interface,
        // not the implementation.
        driver = new FirefoxDriver();
    }

    @After
    public void quit()
    {
        // Close the browser
        driver.quit();
    }

    /**
     * A little test doing a search and then consulting a repository. The test is ignored for now because it won't work
     * on jenkins.
     */
    @Ignore
    @Test
    public void test()
        throws IOException, InterruptedException
    {
        // we enqueue the expected request to github
        githubMock.enqueue( new MockResponse().setResponseCode( 200 )
            .setBody( getMockedBody( "repos/search/play.json" ) ) );

        // opening search page
        driver.get( URL_BASE );

        System.out.println( "Page loading" );

        ( new WebDriverWait( driver, 10 ) ).until( new ExpectedCondition<Boolean>() {
            public Boolean apply( WebDriver d )
            {
                try
                {
                    d.findElement( By.id( "searchKeyword" ) );
                    return true;
                }
                catch ( NoSuchElementException e )
                {
                    return false;
                }
            }
        } );

        // Find the text input element by its name
        WebElement searchKeywordInput = driver.findElement( By.id( "searchKeyword" ) );
        searchKeywordInput.sendKeys( "play" );
        searchKeywordInput.sendKeys( Keys.RETURN );

        System.out.println( "Looking for repository with the keyword play" );

        // we wait that the loading widget disappears
        ( new WebDriverWait( driver, 10 ) ).until( new ExpectedCondition<Boolean>() {
            public Boolean apply( WebDriver d )
            {
                return !d.findElement( By.id( "searchLoading" ) ).isDisplayed();
            }
        } );

        System.out.println( "Search succeeded" );

        assertEquals( URL_BASE + "/#search:keyword=play", driver.getCurrentUrl() );

        WebElement searchResultList = driver.findElement( By.id( "searchResultList" ) );
        List<WebElement> results = searchResultList.findElements( By.xpath( "./div/div[@style='outline:none;']" ) );
        assertEquals( "6 results are expected", 6, results.size() );

        System.out.println( "We select the first result" );

        WebElement firstResultLink = results.get( 0 ).findElement( By.tagName( "a" ) );
        assertEquals( "playframework / play", firstResultLink.getText() );

        // we use a dispatcher because the 3 requests are made simultaneously and we can't predict the order
        githubMock.setDispatcher( new Dispatcher() {

            @Override
            public MockResponse dispatch( RecordedRequest request )
                throws InterruptedException
            {
                try
                {
                    if ( request.getPath().contains( "commits" ) )
                    {
                        return new MockResponse().setResponseCode( 200 ).setBody(
                            getMockedBody( "repos/commits/play.json" ) );
                    }
                    else if ( request.getPath().contains( "collaborators" ) )
                    {
                        return new MockResponse().setResponseCode( 200 ).setBody(
                            getMockedBody( "repos/collaborators/play.json" ) );
                    }
                    return new MockResponse().setResponseCode( 200 ).setBody( getMockedBody( "repos/infos/play.json" ) );
                }
                catch ( Exception e )
                {
                    return null;
                }
            }
        } );

        firstResultLink.click();

        System.out.println( "Waiting that the repo page shows up" );

        // we wait that the loading widget disappears
        ( new WebDriverWait( driver, 10 ) ).until( new ExpectedCondition<Boolean>() {
            public Boolean apply( WebDriver d )
            {
                return !d.findElement( By.id( "repoLoading" ) ).isDisplayed();
            }
        } );

        // we test the presence of the collaborators
        WebElement repoCollaboratorsList = driver.findElement( By.id( "repoCollaboratorsList" ) );
        List<WebElement> collaborators =
            repoCollaboratorsList.findElements( By.xpath( "./div/div[@style='outline:none;']" ) );
        assertEquals( "12 results are expected", 12, collaborators.size() );

        // we test the presence of the title
        WebElement repoTitleContainer = driver.findElement( By.id( "repoTitleContainer" ) );
        WebElement title = repoTitleContainer.findElement( By.tagName( "h4" ) );
        assertEquals( "playframework / play", title.getText() );

        // we test the presence of the commits timeline and collaborators impact
        WebElement repoCommitsTimeline = driver.findElement( By.id( "repoCommitsTimeline" ) );
        assertTrue( repoCommitsTimeline.isDisplayed() );
        // the collaborators impact tab isn't displayed yet
        WebElement repoCollaboratorsImpactChart = driver.findElement( By.id( "repoCollaboratorsImpactChart" ) );
        assertFalse( repoCollaboratorsImpactChart.isDisplayed() );

        List<WebElement> tabs = driver.findElements( By.className( "gwt-TabLayoutPanelTab" ) );
        tabs.get( 1 ).click();
        assertFalse( repoCommitsTimeline.isDisplayed() );
        assertTrue( repoCollaboratorsImpactChart.isDisplayed() );
    }
}
