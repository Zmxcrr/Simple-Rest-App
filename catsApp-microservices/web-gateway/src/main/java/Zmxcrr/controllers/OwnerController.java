package Zmxcrr.controllers;

import Zmxcrr.clients.OwnerClient;
import Zmxcrr.config.RabbitMQConfig;
import Zmxcrr.dto.OwnerDto;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/owners")
@AllArgsConstructor
public class OwnerController {
    private OwnerClient ownerClient;
    private RabbitTemplate rabbitTemplate;

    @GetMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<OwnerDto>> getAll() {
        return ownerClient.getAll();
    }

    @PostMapping(value = "")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> save(@RequestBody OwnerDto owner) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.CREATE_OWNER_QUEUE, owner);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @authorizeService.isCurrentOwner(authentication, #id.longValue())")
    public ResponseEntity<?> getById(@PathVariable long id) {
        return ownerClient.getById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable long id) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.DELETE_OWNER_QUEUE, id);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{id}/cats")
    @PreAuthorize("hasAuthority('ADMIN') or @authorizeService.isCurrentOwner(authentication, #id.longValue())")
    public ResponseEntity<?> getCats(@PathVariable long id) {
        return ownerClient.getCats(id);
    }
}
