package com.github.nmorel.homework.client;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.github.nmorel.homework.AbstractIT;

public abstract class AbstractSeleniumIT
    extends AbstractIT
{
    protected WebDriver driver;

    @Before
    public void init()
        throws Exception
    {
        this.driver = new FirefoxDriver();
    }

    @After
    public void quit()
    {
        // Close the browser
        driver.quit();
    }
}
