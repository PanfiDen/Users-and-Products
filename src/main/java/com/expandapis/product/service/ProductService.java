package com.expandapis.product.service;

import com.expandapis.handler.exceptions.BadRequestException;
import com.expandapis.handler.exceptions.ProductNotFoundException;
import com.expandapis.product.model.request.ProductsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private static final String CREATE_TABLE_TEMPLATE = "CREATE TABLE %s (%s)";
    private static final String INSERT_INTO_TEMPLATE = "INSERT INTO %s (%s) VALUES (%s)";
    private static final String SELECT_TABLE_COUNT = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE UPPER(TABLE_NAME) = UPPER(?)";

    private final JdbcTemplate jdbcTemplate;

    public String processTableData(ProductsRequest productsRequest) {
        String tableName = productsRequest.table();

        try {
            if (tableExists(tableName)) {

                insertDataIntoTable(productsRequest, tableName);
            } else {
                createTableAndInsertData(productsRequest, tableName);
            }
        } catch (Exception e) {
                throw new BadRequestException("Oops! Something went wrong");
        }
        return "SUCCESS!";
    }

    public List<Map<String, Object>> getAll(String tableName) {
        if (tableName.isBlank() || !tableExists(tableName)) {
            throw new ProductNotFoundException();
        }

        String query = String.format("SELECT * FROM %s", tableName);
        return jdbcTemplate.queryForList(query);
    }

    private void createTableAndInsertData(ProductsRequest productsRequest, String tableName) {
        String createTableQuery = buildCreateTableQuery(productsRequest, tableName);
        jdbcTemplate.execute(createTableQuery);
        insertDataIntoTable(productsRequest, tableName);
    }

    private void insertDataIntoTable(ProductsRequest productsRequest, String tableName) {
        productsRequest.records().forEach(record -> executeInsertQuery(tableName, record));
    }

    private String buildCreateTableQuery(ProductsRequest productsRequest, String tableName) {
        String columns = productsRequest.records().get(0).keySet().stream()
                .map(column -> column + " VARCHAR(255)")
                .collect(Collectors.joining(", "));

        return String.format(CREATE_TABLE_TEMPLATE, tableName, columns);
    }

    private void executeInsertQuery(String tableName, Map<String, String> record) {
        try (Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            String columns = String.join(", ", record.keySet());
            String values = record.values().stream().map(value -> "'" + value + "'").collect(Collectors.joining(", "));
            String query = String.format(INSERT_INTO_TEMPLATE, tableName, columns, values);

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new BadRequestException("Oops! Something went wrong!");
        }
    }

    private boolean tableExists(String tableName) {
        int count = jdbcTemplate.queryForObject(SELECT_TABLE_COUNT, Integer.class, tableName);
        return count > 0;
    }
}

