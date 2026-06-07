package com.company.sdet.tests.api;

import com.company.sdet.base.BaseApiTest;
import com.company.sdet.utils.JsonContractValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.InputStream;

public class FakeStoreContractTest extends BaseApiTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Test(description = "Cart response should match saved contract shape")
    public void cartResponseMatchesSavedContract() throws Exception {
        APIResponse response = request.get("/carts/1");
        Assert.assertEquals(response.status(), 200);

        JsonNode actualCart = mapper.readTree(response.text());
        InputStream contractStream = getClass().getClassLoader()
                .getResourceAsStream("testdata/cart-response-contract.json");

        Assert.assertNotNull(contractStream, "Contract file should exist in test resources.");
        JsonNode expectedContract = mapper.readTree(contractStream);
        JsonContractValidator.assertMatchesShape(actualCart, expectedContract);
    }
}
