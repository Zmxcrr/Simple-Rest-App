package Zmxcrr.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class OwnerDto {
    Long id;
    String firstName;
    String lastName;
    LocalDate birthdate;
}