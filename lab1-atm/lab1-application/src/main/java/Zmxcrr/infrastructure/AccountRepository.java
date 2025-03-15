package Zmxcrr.infrastructure;

import Zmxcrr.models.Money;
import Zmxcrr.models.accounts.Account;
import Zmxcrr.models.accounts.AccountStatus;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    void addAccount(Account account);
    void updateBalance(Account account, Money value);
    void updateStatus(Account account, AccountStatus status);
    Optional<Account> getAccountById(UUID id);
}