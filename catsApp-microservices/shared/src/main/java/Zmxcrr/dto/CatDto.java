package Zmxcrr.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class CatDto {
    Long id;
    String name;
    LocalDate birthdate;
    String breed;
    String color;
    Long owner;
    List<Long> friends;
}