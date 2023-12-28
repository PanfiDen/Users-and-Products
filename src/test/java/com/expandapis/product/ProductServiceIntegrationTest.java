package com.expandapis.product;

import com.expandapis.handler.exceptions.BadRequestException;
import com.expandapis.handler.exceptions.ProductNotFoundException;
import com.expandapis.product.model.request.ProductsRequest;
import com.expandapis.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @Test
    void testProcessTableDataAndGetData() {
        ProductsRequest productsRequest = new ProductsRequest("test_table",
                Arrays.asList(
                        Map.of("column1", "value1"),
                        Map.of("column1", "value2")
                ));

        String result = productService.processTableData(productsRequest);
        assertEquals("SUCCESS!", result);

        List<Map<String, Object>> data = productService.getAll("test_table");
        assertEquals(2, data.size());
        assertEquals("value1", data.get(0).get("column1"));
        assertEquals("value2", data.get(1).get("column1"));
    }

    @Test
    void testGetAllWithInvalidTableName() {
        assertThrows(ProductNotFoundException.class, () -> productService.getAll("nonexistent_table"));
    }

    @Test
    void testProcessTableDataWithSQLException() {
        ProductsRequest productsRequest = new ProductsRequest("test_table",
                Arrays.asList(
                        Map.of("column1", "value1"),
                        Map.of("column1", "value2", "column2", "invalid_value")
                ));

        assertThrows(BadRequestException.class, () -> productService.processTableData(productsRequest));
    }
}


