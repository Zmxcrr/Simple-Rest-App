package Zmxcrr.contracts;

import Zmxcrr.models.User;
import Zmxcrr.models.accounts.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountService {
    Account createAccount(User user);
    Optional<Account> getAccountById(UUID id);
    List<Account> getAccountsByUser(User user);;
}