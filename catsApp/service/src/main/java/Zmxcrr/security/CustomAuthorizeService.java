package Zmxcrr.security;


import Zmxcrr.dto.CatDto;
import Zmxcrr.repositories.CatRepository;
import Zmxcrr.repositories.OwnerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@Component("authorizeService")
public class CustomAuthorizeService {
    private CatRepository catRepository;
    private OwnerRepository ownerRepository;

    public boolean hasAccessToCat(Authentication auth, CatDto cat) {
        var user = (UserDetailsImpl) auth.getPrincipal();
        if (cat == null)
            return true;

        return cat.getOwner().equals(user.getUser().getOwner());
    }

    public boolean hasAccessToCatID(Authentication auth, long catId) {
        var user = (UserDetailsImpl) auth.getPrincipal();
        var cat = catRepository.findById(catId);

        if (cat.isEmpty())
            return true;
        System.out.println(cat.get());

        return cat.get().getOwner().getId().equals(user.getUser().getOwner());
    }

    public boolean isCurrentOwner(Authentication auth, long ownerId) {
        var user = (UserDetailsImpl) auth.getPrincipal();
        return user.getUser().getOwner().equals(ownerId);
    }

    public boolean isOneOfFriends(Authentication auth, CatDto cat) {
        var user = (UserDetailsImpl) auth.getPrincipal();
        Long ownerId = user.getUser().getOwner();
        for (var c : ownerRepository.findById(ownerId).get().getCats())
            for (var friend : c.getFriends())
                if (friend.getId() == cat.getId())
                    return true;

        return false;
    }
}