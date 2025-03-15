package Zmxcrr.contracts;

import Zmxcrr.models.accounts.Account;

public interface AccountOperationService {
    ServiceOperationResult deposit(Account account, double value);
    ServiceOperationResult withdraw(Account account, double value);
}