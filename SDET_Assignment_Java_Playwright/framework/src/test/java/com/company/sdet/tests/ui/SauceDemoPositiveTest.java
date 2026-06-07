package com.company.sdet.tests.ui;

import com.company.sdet.base.BaseUiTest;
import com.company.sdet.pages.CartPage;
import com.company.sdet.pages.InventoryPage;
import com.company.sdet.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SauceDemoPositiveTest extends BaseUiTest {

    @Test(description = "Standard user can login, add item to cart and complete checkout")
    public void standardUserCanCompleteCheckout() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.login("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(page);
        Assert.assertTrue(inventoryPage.isProductListVisible(), "Inventory page should be visible after login.");

        inventoryPage.addBackpackToCart();
        Assert.assertEquals(inventoryPage.getCartCount(), "1", "Cart badge should show one item.");

        inventoryPage.openCart();
        CartPage cartPage = new CartPage(page);
        Assert.assertTrue(cartPage.isBackpackVisibleInCart(), "Selected item should be visible in cart.");

        cartPage.completeCheckout("Shantanu", "Jha", "560001");
        Assert.assertEquals(cartPage.getConfirmationMessage(), "Thank you for your order!");
    }
}
