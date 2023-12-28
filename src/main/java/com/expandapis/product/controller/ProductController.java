package com.expandapis.product.controller;

import com.expandapis.product.model.request.ProductsRequest;
import com.expandapis.product.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product/")
@AllArgsConstructor
public class ProductController {
    private ProductService productService;

    @PostMapping("add")
    public ResponseEntity<String> add(@RequestBody ProductsRequest productsRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.processTableData(productsRequest));
    }

    @GetMapping("/all/{tableName}")
    public ResponseEntity<List<Map<String, Object>>> getAllRowsByTableName(@PathVariable(name = "tableName") String tableName) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getAll(tableName));
    }
}
