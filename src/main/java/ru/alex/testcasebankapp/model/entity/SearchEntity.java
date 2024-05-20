package ru.alex.testcasebankapp.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.alex.testcasebankapp.model.SearchType;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
public class SearchEntity {

    @Schema(example = "2024-05-17T18:29:34.123Z", description = "параметр для поиска всех элементов больше данной даты")
    private LocalDateTime date;

    @Schema(example = "+79991112211", description = "параметр для поиска по 100% сходству по телефону")
    private String phone;

    @Schema(example = "i%", description = "параметр для поиска всех элементов по шаблону")
    private String fullName;

    @Schema(example = "1111111@gmail.com", description = "параметр для поиска по 100% сходству по почте")
    private String email;

    public SearchType getType() {
        if (Optional.ofNullable(this.email).isPresent()) {
            return SearchType.EMAIL;
        }
        if (Optional.ofNullable(this.phone).isPresent()) {
            return SearchType.PHONE;
        }
        if (Optional.ofNullable(this.date).isPresent()) {
            return SearchType.DATE;
        }
        if (Optional.ofNullable(this.fullName).isPresent()) {
            return SearchType.FULLNAME;
        } else {
            return SearchType.NONE;
        }
    }
}
