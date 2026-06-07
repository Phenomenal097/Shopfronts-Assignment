package com.company.sdet.tests.ui;

import com.company.sdet.base.BaseUiTest;
import com.company.sdet.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SauceDemoNegativeTest extends BaseUiTest {

    @Test(description = "Locked out user should not be able to login")
    public void lockedOutUserCannotLogin() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.login("locked_out_user", "secret_sauce");

        Assert.assertTrue(loginPage.getErrorMessage().contains("locked out"),
                "Locked user should see locked out error message.");
    }

    @Test(description = "Invalid password should display login error")
    public void invalidPasswordShowsError() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.login("standard_user", "wrong_password");

        Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match"),
                "Invalid password should show credential mismatch error.");
    }
}
