package Zmxcrr.controllers;

import Zmxcrr.dto.UserDto;
import Zmxcrr.enums.CatColor;
import Zmxcrr.services.CatService;
import Zmxcrr.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    private CatService catService;
    private UserService userService;

    @GetMapping("/cats")
    public ResponseEntity<?> getAllCats(
            @RequestParam(required = false) CatColor color,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) Integer year
    ) {
        return ResponseEntity.ok(catService.findFiltered(color, breed, year, null));
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    @PostMapping("/users")
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto) {
        userService.create(
                userDto.getUsername(),
                userDto.getPassword(),
                userDto.getRole(),
                userDto.getOwner());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users")
    public ResponseEntity<?> deleteUser(@RequestParam String username) {
        userService.delete(username);
        return ResponseEntity.ok().build();
    }
}
