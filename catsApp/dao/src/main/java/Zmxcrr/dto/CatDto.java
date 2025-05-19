package Zmxcrr.dto;

import Zmxcrr.entities.Cat;
import Zmxcrr.enums.CatColor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class    CatDto {
    Long id;
    String name;
    LocalDate birthdate;
    String breed;
    CatColor color;
    Long owner;
    List<Long> friends;

    public CatDto(Cat cat) {
        if (cat != null) {
            this.id = cat.getId();
            this.name = cat.getName();
            this.birthdate = cat.getBirthdate();
            this.breed = cat.getBreed();
            this.color = cat.getColor();
            this.owner = cat.getOwner().getId();
            for (var friend : cat.getFriends())
                friends.add(friend.getId());
        }
    }
}