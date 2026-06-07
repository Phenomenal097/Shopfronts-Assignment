package com.company.sdet.base;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.HashMap;
import java.util.Map;

public class BaseApiTest {
    protected Playwright playwright;
    protected APIRequestContext request;

    @BeforeClass
    public void setUpApiClient() {
        playwright = Playwright.create();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        request = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL("https://fakestoreapi.com")
                .setExtraHTTPHeaders(headers));
    }

    @AfterClass(alwaysRun = true)
    public void tearDownApiClient() {
        if (request != null) {
            request.dispose();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}
