package com.company.sdet.tests.api;

import com.company.sdet.base.BaseApiTest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class FakeStoreCartCrudTest extends BaseApiTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Test(description = "Create cart with valid user and product details")
    public void createCartWithValidPayload() throws Exception {
        Map<String, Object> payload = cartPayload(5, List.of(product(1, 2)));

        APIResponse response = request.post("/carts", RequestOptions.create().setData(payload));
        assertOkOrCreated(response);

        JsonNode body = mapper.readTree(response.text());
        Assert.assertTrue(body.has("id"), "Create cart response should contain id.");
        Assert.assertEquals(body.get("userId").asInt(), 5);
        Assert.assertEquals(body.get("products").get(0).get("productId").asInt(), 1);
    }

    @Test(description = "Get cart details by cart id")
    public void getCartById() throws Exception {
        APIResponse response = request.get("/carts/1");
        Assert.assertEquals(response.status(), 200);

        JsonNode body = mapper.readTree(response.text());
        Assert.assertEquals(body.get("id").asInt(), 1);
        Assert.assertTrue(body.get("products").isArray(), "Products should be returned as array.");
    }

    @Test(description = "Update existing cart details")
    public void updateCart() throws Exception {
        Map<String, Object> payload = cartPayload(3, List.of(product(2, 4), product(4, 1)));

        APIResponse response = request.put("/carts/1", RequestOptions.create().setData(payload));
        Assert.assertEquals(response.status(), 200);

        JsonNode body = mapper.readTree(response.text());
        Assert.assertEquals(body.get("userId").asInt(), 3);
        Assert.assertEquals(body.get("products").size(), 2);
    }

    @Test(description = "Delete cart by cart id")
    public void deleteCart() throws Exception {
        APIResponse response = request.delete("/carts/1");
        Assert.assertEquals(response.status(), 200);

        JsonNode body = mapper.readTree(response.text());
        Assert.assertEquals(body.get("id").asInt(), 1);
    }

    @Test(description = "Unknown cart id should not return a usable cart")
    public void getUnknownCartId() {
        APIResponse response = request.get("/carts/999999");
        Assert.assertTrue(response.status() == 200 || response.status() == 404,
                "FakeStore demo API may return 200 with null body or 404 for unknown cart.");
    }

    @Test(description = "Incomplete cart payload should be handled")
    public void createCartWithMissingProducts() throws Exception {
        Map<String, Object> payload = Map.of("userId", 5, "date", "2026-06-07");

        APIResponse response = request.post("/carts", RequestOptions.create().setData(payload));
        Assert.assertTrue(response.status() >= 200 && response.status() < 500,
                "API should return a deterministic response for incomplete payload.");

        JsonNode body = mapper.readTree(response.text());
        Assert.assertFalse(body.hasNonNull("products") && body.get("products").size() > 0,
                "Incomplete request should not create product rows silently.");
    }

    @Test(dataProvider = "productIds", description = "Data-driven cart creation for multiple product ids")
    public void createCartForDifferentProducts(int productId) throws Exception {
        Map<String, Object> payload = cartPayload(2, List.of(product(productId, 1)));

        APIResponse response = request.post("/carts", RequestOptions.create().setData(payload));
        assertOkOrCreated(response);

        JsonNode body = mapper.readTree(response.text());
        Assert.assertEquals(body.get("products").get(0).get("productId").asInt(), productId);
    }

    @DataProvider(name = "productIds")
    public Object[][] productIds() {
        return new Object[][]{{1}, {2}, {3}, {4}};
    }

    private Map<String, Object> cartPayload(int userId, List<Map<String, Integer>> products) {
        return Map.of(
                "userId", userId,
                "date", "2026-06-07",
                "products", products
        );
    }

    private Map<String, Integer> product(int productId, int quantity) {
        return Map.of("productId", productId, "quantity", quantity);
    }

    private void assertOkOrCreated(APIResponse response) {
        Assert.assertTrue(response.status() == 200 || response.status() == 201,
                "Expected 200 OK or 201 Created but got: " + response.status());
    }
}
