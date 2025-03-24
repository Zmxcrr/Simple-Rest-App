package Zmxcrr;

import Zmxcrr.contracts.*;
import Zmxcrr.infrastructure.*;
import Zmxcrr.services.*;

public class Main {
    public static void main(String[] args) {
        AccountRepository accountRepository = new InMemoryAccountRepository();
        UserRepository userRepository = new InMemoryUserRepository();
        TransactionRepository transactionRepository = new InMemoryTransactionRepository();

        UserService userService = new UserServiceImpl(userRepository);
        AccountService accountService = new AccountServiceImpl(accountRepository, userService);
        TransactionService transactionService = new TransactionServiceImpl(transactionRepository);
        AccountOperationService accountOperationService = new AccountOperationServiceImpl(accountRepository, transactionService);

        ATMConsoleInterface console = new ATMConsoleInterface(
                accountService,
                userService,
                accountOperationService,
                transactionService);

        console.start();
    }
}