package com.github.nmorel.homework.client;

import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.github.nmorel.homework.AbstractIT;
import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.junit.SauceOnDemandTestWatcher;

public abstract class AbstractSeleniumIT
    extends AbstractIT
    implements SauceOnDemandSessionIdProvider
{
    /**
     * Constructs a {@link SauceOnDemandAuthentication} instance using the supplied user name/access key. To use the
     * authentication supplied by environment variables or from an external file, use the no-arg
     * {@link SauceOnDemandAuthentication} constructor.
     */
    public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication();

    /**
     * JUnit Rule which will mark the Sauce Job as passed/failed when the test succeeds or fails.
     */
    @Rule
    public SauceOnDemandTestWatcher resultReportingTestWatcher = new SauceOnDemandTestWatcher( this, authentication );

    /**
     * JUnit Rule which will record the test name of the current test. This is referenced when creating the
     * {@link DesiredCapabilities}, so that the Sauce Job is created with the test name.
     */
    @Rule
    public TestName testName = new TestName();

    protected WebDriver driver;

    protected String sessionId;

    @Before
    public void init()
        throws Exception
    {
        if ( null == authentication.getUsername() || null == authentication.getAccessKey() )
        {
            // for local test
            this.driver = new FirefoxDriver();
        }
        else
        {
            // for jenkins tests
            DesiredCapabilities capabillities = DesiredCapabilities.firefox();
            capabillities.setCapability( "version", "16" );
            capabillities.setCapability( "platform", Platform.LINUX );
            capabillities.setCapability( "name", testName.getMethodName() );
            this.driver =
                new RemoteWebDriver( new URL( "http://" + authentication.getUsername() + ":"
                    + authentication.getAccessKey() + "@ondemand.saucelabs.com:80/wd/hub" ), capabillities );
            this.sessionId = ( (RemoteWebDriver) driver ).getSessionId().toString();
        }
    }

    @After
    public void quit()
    {
        // Close the browser
        driver.quit();
    }

    @Override
    public String getSessionId()
    {
        return sessionId;
    }
}
