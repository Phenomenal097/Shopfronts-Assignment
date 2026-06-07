package com.company.sdet.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class LoginPage {
    private final Locator usernameInput;
    private final Locator passwordInput;
    private final Locator loginButton;
    private final Locator errorMessage;

    public LoginPage(Page page) {
        usernameInput = page.locator("[data-test='username']");
        passwordInput = page.locator("[data-test='password']");
        loginButton = page.locator("[data-test='login-button']");
        errorMessage = page.locator("[data-test='error']");
    }

    public void login(String username, String password) {
        usernameInput.fill(username);
        passwordInput.fill(password);
        loginButton.click();
    }

    public String getErrorMessage() {
        return errorMessage.innerText();
    }
}
