package com.company.sdet.utils;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Iterator;
import java.util.Map;

import static org.testng.Assert.assertTrue;

public final class JsonContractValidator {
    private JsonContractValidator() {
    }

    public static void assertMatchesShape(JsonNode actual, JsonNode expectedContract) {
        Iterator<Map.Entry<String, JsonNode>> fields = expectedContract.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String fieldName = field.getKey();
            JsonNode expectedShape = field.getValue();

            assertTrue(actual.has(fieldName), "Missing field in response: " + fieldName);
            JsonNode actualValue = actual.get(fieldName);

            if (expectedShape.isTextual()) {
                assertType(actualValue, expectedShape.asText(), fieldName);
            } else if (expectedShape.isObject()) {
                assertTrue(actualValue.isObject(), "Expected object field: " + fieldName);
                assertMatchesShape(actualValue, expectedShape);
            } else if (expectedShape.isArray()) {
                assertTrue(actualValue.isArray(), "Expected array field: " + fieldName);
                if (!actualValue.isEmpty() && !expectedShape.isEmpty()) {
                    assertMatchesShape(actualValue.get(0), expectedShape.get(0));
                }
            }
        }
    }

    private static void assertType(JsonNode actualValue, String expectedType, String fieldName) {
        switch (expectedType) {
            case "number" -> assertTrue(actualValue.isNumber(), "Expected numeric field: " + fieldName);
            case "string" -> assertTrue(actualValue.isTextual(), "Expected string field: " + fieldName);
            case "boolean" -> assertTrue(actualValue.isBoolean(), "Expected boolean field: " + fieldName);
            default -> throw new IllegalArgumentException("Unsupported contract type: " + expectedType);
        }
    }
}
