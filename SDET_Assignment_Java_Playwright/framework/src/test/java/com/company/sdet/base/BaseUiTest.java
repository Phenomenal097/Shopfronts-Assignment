package com.company.sdet.base;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.nio.file.Files;
import java.nio.file.Path;

public class BaseUiTest {
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeMethod
    public void setUpBrowser() {
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));
        playwright = Playwright.create();

        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(headless);
        Path localChrome = Path.of("/Applications/Google Chrome.app/Contents/MacOS/Google Chrome");
        if (Files.exists(localChrome)) {
            launchOptions.setExecutablePath(localChrome);
        }

        browser = playwright.chromium().launch(launchOptions);
        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1366, 768));
        page = context.newPage();
        page.navigate("https://www.saucedemo.com/");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownBrowser() {
        if (context != null) {
            context.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}
