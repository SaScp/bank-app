package ru.alex.testcasebankapp.service.rowmapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JsonNodeRowMapper implements RowMapper<JsonNode> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public JsonNode mapRow(ResultSet rs, int rowNum) throws SQLException {
        int columnCount = rs.getMetaData().getColumnCount();
        JsonNode node = objectMapper.createObjectNode();
        for (int i = 1; i <= columnCount; i++) {
            ((ObjectNode) node).put(rs.getMetaData().getColumnName(i), rs.getString(i));
        }
        return node;
    }
}