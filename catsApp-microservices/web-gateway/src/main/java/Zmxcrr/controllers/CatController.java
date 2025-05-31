package Zmxcrr.controllers;

import Zmxcrr.clients.CatClient;
import Zmxcrr.config.RabbitMQConfig;
import Zmxcrr.dto.CatDto;
import Zmxcrr.dto.FriendshipDto;
import Zmxcrr.security.UserDetailsImpl;
import Zmxcrr.users.enums.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/cats")
@AllArgsConstructor
public class CatController {
    private CatClient catClient;
    private RabbitTemplate rabbitTemplate;
    @GetMapping("")
    public ResponseEntity<List<CatDto>> filter(
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Long owner
    ) {
        var user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        if (user.getRole() == UserRole.ADMIN)
            return catClient.filter(color, breed, year, owner);

        if (owner == null)
            return catClient.filter(color, breed, year, user.getOwner());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('ADMIN') or @authorizeService.isCurrentOwner(authentication, #cat.owner.longValue())")
    public ResponseEntity<?> save(@RequestBody CatDto cat) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.CREATE_CAT_QUEUE, cat);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasAuthority('ADMIN') or @authorizeService.hasAccessToCatID(authentication, #id.longValue())")
    public ResponseEntity<CatDto> getById(@PathVariable Long id) {
        return catClient.getById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @authorizeService.hasAccessToCatID(authentication, #id.longValue())")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.DELETE_CAT_QUEUE, id);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{id}/friends")
    @PreAuthorize("hasAuthority('ADMIN') or @authorizeService.hasAccessToCatID(authentication, #id.longValue())")
    public ResponseEntity<?> findFriends(@PathVariable Long id) {
        return catClient.findFriends(id);
    }

    @PostMapping("/{id}/friends")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> makeFriend(
            @PathVariable long id,
            @RequestParam long friendId
    ) {
        var friendship = new FriendshipDto();
        friendship.setFirstCatId(id);
        friendship.setSecondCatId(friendId);

        rabbitTemplate.convertAndSend(RabbitMQConfig.CREATE_FRIEND_QUEUE, friendship);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("{id}/friends")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteFriend(
            @PathVariable long id,
            @RequestParam long friendId
    ) {
        var friendship = new FriendshipDto();
        friendship.setFirstCatId(id);
        friendship.setSecondCatId(friendId);

        rabbitTemplate.convertAndSend(RabbitMQConfig.DELETE_FRIEND_QUEUE, friendship);
        return ResponseEntity.accepted().build();
    }
}
