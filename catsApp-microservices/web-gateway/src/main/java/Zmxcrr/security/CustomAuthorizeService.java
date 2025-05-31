package Zmxcrr.security;

import Zmxcrr.clients.CatClient;
import Zmxcrr.dto.CatDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@Component("authorizeService")
public class CustomAuthorizeService {
    private CatClient catClient;

    public boolean hasAccessToCat(Authentication auth, CatDto cat) {
        var user = (UserDetailsImpl) auth.getPrincipal();
        if (cat == null)
            return true;

        return cat.getOwner().equals(user.getUser().getOwner());
    }

    public boolean hasAccessToCatID(Authentication auth, long catId) {
        var user = (UserDetailsImpl) auth.getPrincipal();
        var response = catClient.getById(catId);

        if (!response.getStatusCode().isSameCodeAs(HttpStatus.OK) || response.getBody() == null)
            return true;

        return response.getBody().getOwner().equals(user.getUser().getOwner());
    }

    public boolean isCurrentOwner(Authentication auth, long ownerId) {
        var user = (UserDetailsImpl) auth.getPrincipal();
        return user.getUser().getOwner().equals(ownerId);
    }
}

