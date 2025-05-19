package Zmxcrr.controllers;

import Zmxcrr.dto.OwnerDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import Zmxcrr.services.OwnerService;


import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/owners")
public class OwnerController {
    private final OwnerService ownerService;

    @GetMapping("")
    public ResponseEntity<?> getAll(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        var page = ownerService.getAllOwners(pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping("")
    public ResponseEntity<?> save(@RequestBody OwnerDto owner) {
        var id = ownerService.createOwner(
                owner.getFirstName(),
                owner.getLastName(),
                owner.getBirthdate());
        return ResponseEntity.ok(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OwnerDto> getById(@PathVariable long id) {
        var owner = ownerService.getById(id);
        if (owner.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(owner.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        ownerService.removeOwner(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/cats")
    public ResponseEntity<?> getCats(@PathVariable long id) {
        var cats = ownerService.getAllCats(id);
        return ResponseEntity.ok(cats);
    }
}