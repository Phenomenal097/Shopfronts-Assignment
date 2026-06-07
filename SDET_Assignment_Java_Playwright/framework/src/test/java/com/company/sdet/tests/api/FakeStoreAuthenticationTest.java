package com.company.sdet.tests.api;

import com.company.sdet.base.BaseApiTest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class FakeStoreAuthenticationTest extends BaseApiTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Test(description = "Valid login should return token")
    public void validLoginReturnsToken() throws Exception {
        Map<String, String> payload = Map.of(
                "username", "mor_2314",
                "password", "83r5^_"
        );

        APIResponse response = request.post("/auth/login", RequestOptions.create().setData(payload));
        Assert.assertTrue(response.status() == 200 || response.status() == 201,
                "Valid login should return successful status.");

        JsonNode body = mapper.readTree(response.text());
        Assert.assertTrue(body.hasNonNull("token"), "Valid login should return token.");
    }

    @Test(description = "Invalid login should not return token")
    public void invalidLoginDoesNotReturnToken() throws Exception {
        Map<String, String> payload = Map.of(
                "username", "invalid_user",
                "password", "invalid_password"
        );

        APIResponse response = request.post("/auth/login", RequestOptions.create().setData(payload));
        Assert.assertTrue(response.status() >= 200 && response.status() < 500,
                "Invalid login should return handled response.");

        String responseText = response.text();
        if (responseText != null && responseText.trim().startsWith("{")) {
            JsonNode body = mapper.readTree(responseText);
            Assert.assertFalse(body.hasNonNull("token"), "Invalid login should not return token.");
        } else {
            Assert.assertTrue(responseText == null || !responseText.toLowerCase().contains("token"),
                    "Invalid login response should not contain token.");
        }
    }
}
