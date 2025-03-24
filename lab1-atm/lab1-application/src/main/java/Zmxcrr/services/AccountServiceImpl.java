package Zmxcrr.services;

import Zmxcrr.contracts.AccountService;
import Zmxcrr.contracts.UserService;
import Zmxcrr.infrastructure.AccountRepository;
import Zmxcrr.models.Money;
import Zmxcrr.models.User;
import Zmxcrr.models.accounts.Account;
import Zmxcrr.models.accounts.AccountStatus;
import Zmxcrr.models.transactions.Transaction;

import java.util.*;
import java.util.stream.Collectors;

public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final UserService userService;

    public AccountServiceImpl(AccountRepository accountRepository, UserService userService) {
        this.accountRepository = accountRepository;
        this.userService = userService;
    }

    @Override
    public Account createAccount(User user) {
        UUID id = UUID.randomUUID();
        Calendar creationDate = Calendar.getInstance();
        List<Transaction> transactions = new ArrayList<>();
        Money initialBalance = new Money(0.0);
        AccountStatus status = AccountStatus.ACTIVE;

        Account account = new Account(id, creationDate, transactions, initialBalance, status);
        accountRepository.addAccount(account);
        userService.linkAccountToUser(user.getId(), account);

        return account;
    }

    @Override
    public Optional<Account> getAccountById(UUID id) {
        return accountRepository.getAccountById(id);
    }

    @Override
    public List<Account> getAccountsByUser(User user) {
        return user.getAccountIds().stream()
                .map(accountRepository::getAccountById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}