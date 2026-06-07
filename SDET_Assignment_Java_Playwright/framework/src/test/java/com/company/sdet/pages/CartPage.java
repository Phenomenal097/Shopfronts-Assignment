package com.company.sdet.pages;

import com.microsoft.playwright.Page;

public class CartPage {
    private final Page page;

    public CartPage(Page page) {
        this.page = page;
    }

    public boolean isBackpackVisibleInCart() {
        return page.locator(".inventory_item_name").filter().isVisible();
    }

    public void completeCheckout(String firstName, String lastName, String postalCode) {
        page.locator("[data-test='checkout']").click();
        page.locator("[data-test='firstName']").fill(firstName);
        page.locator("[data-test='lastName']").fill(lastName);
        page.locator("[data-test='postalCode']").fill(postalCode);
        page.locator("[data-test='continue']").click();
        page.locator("[data-test='finish']").click();
    }

    public String getConfirmationMessage() {
        return page.locator(".complete-header").innerText();
    }
}
