package com.company.sdet.pages;

import com.microsoft.playwright.Page;

public class InventoryPage {
    private final Page page;

    public InventoryPage(Page page) {
        this.page = page;
    }

    public boolean isProductListVisible() {
        return page.locator(".inventory_item").first().isVisible();
    }

    public void addBackpackToCart() {
        page.locator("[data-test='add-to-cart-sauce-labs-backpack']").click();
    }

    public String getCartCount() {
        return page.locator(".shopping_cart_badge").innerText();
    }

    public void openCart() {
        page.locator(".shopping_cart_link").click();
    }
}
