package Zmxcrr.controllers;

import Zmxcrr.clients.CatClient;
import Zmxcrr.dto.CatDto;
import Zmxcrr.users.dto.UserDto;
import Zmxcrr.users.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    private CatClient catClient;
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    @GetMapping("/cats")
    public ResponseEntity<List<CatDto>> getAllCats(
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) Integer year
    ) {
        return catClient.filter(color, breed, year, null);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    @PostMapping("/users")
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto) {
        userService.create(
                userDto.getUsername(),
                passwordEncoder.encode(userDto.getPassword()),
                userDto.getRole(),
                userDto.getOwner());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users")
    public ResponseEntity<?> deleteUser(@RequestParam String username) {
        userService.delete(username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        var optional = userService.getByUsername(username);
        if (optional.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(optional.get());
    }
}
