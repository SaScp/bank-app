package ru.alex.testcasebankapp.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationEntity {

    private int pageSize;

    private int pageNumber;

    private String propertySort;
}
