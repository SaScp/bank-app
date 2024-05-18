package ru.alex.testcasebankapp.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationEntity {

    private int pageSize;

    private int pageNumber;

    private String propertySort;
}
