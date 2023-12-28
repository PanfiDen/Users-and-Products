package com.expandapis.product.model.request;

import java.util.List;
import java.util.Map;

public record ProductsRequest(String table, List<Map<String, String>> records) {
}
