package ru.alex.testcasebankapp.model.entity;

import lombok.Data;
import ru.alex.testcasebankapp.model.SearchType;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
public class SearchEntity {

    private LocalDateTime date;

    private String phone;

    private String fullName;

    private String email;

    private int pageSize;

    private int pageNumber;

    public SearchType getType() {
        if (Optional.ofNullable(this.email).isPresent()) {
            return SearchType.EMAIL;
        }
        if (Optional.ofNullable(this.phone).isPresent()) {
            return SearchType.PHONE;
        }
        if(Optional.ofNullable(this.date).isPresent()) {
            return SearchType.DATE;
        }
        if(Optional.ofNullable(this.fullName).isPresent())  {
            return SearchType.FULLNAME;
        } else {
            return SearchType.NONE;
        }
    }
}
