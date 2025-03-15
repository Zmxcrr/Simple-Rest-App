package Zmxcrr.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class User {
    private UUID id;
    private String name;
    private PinCode pinCode;
    private List<UUID> accountIds;
}