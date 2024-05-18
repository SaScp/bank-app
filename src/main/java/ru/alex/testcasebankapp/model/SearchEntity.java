package ru.alex.testcasebankapp.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SearchEntity {

    private LocalDateTime date;

    private String phone;

    private String fullName;

    private String email;

    private int pageSize;

    private int pageNumber;

    public SearchType getType() {
        if (email != null) {
            return SearchType.EMAIL;
        }
        if (phone != null) {
            return SearchType.PHONE;
        }
        if(date != null) {
            return SearchType.DATE;
        }
        else  {
            return SearchType.FULLNAME;
        }
    }
}
