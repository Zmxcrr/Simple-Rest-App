import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import Zmxcrr.InMemoryAccountRepository;
import Zmxcrr.InMemoryTransactionRepository;
import Zmxcrr.InMemoryUserRepository;
import Zmxcrr.contracts.ServiceOperationResult;
import Zmxcrr.models.Money;
import Zmxcrr.models.PinCode;
import Zmxcrr.models.User;
import Zmxcrr.models.accounts.Account;
import Zmxcrr.models.accounts.AccountStatus;
import Zmxcrr.models.transactions.Transaction;
import Zmxcrr.models.transactions.TransactionStatus;
import Zmxcrr.services.AccountOperationServiceImpl;
import Zmxcrr.services.AccountServiceImpl;
import Zmxcrr.services.TransactionServiceImpl;
import Zmxcrr.services.UserServiceImpl;

public class AtmBusinessLogicTests {

    private InMemoryAccountRepository accountRepository;
    private InMemoryTransactionRepository transactionRepository;
    private InMemoryUserRepository userRepository;
    private AccountOperationServiceImpl accountOperationService;
    private AccountServiceImpl accountService;
    private TransactionServiceImpl transactionService;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        accountRepository = new InMemoryAccountRepository();
        transactionRepository = new InMemoryTransactionRepository();
        userRepository = new InMemoryUserRepository();

        userService = new UserServiceImpl(userRepository);
        transactionService = new TransactionServiceImpl(transactionRepository);
        accountOperationService = new AccountOperationServiceImpl(accountRepository, transactionService);
        accountService = new AccountServiceImpl(accountRepository, userService);
    }

    @Test
    void testAddAccount_WithExistingId_ThrowsException() {
        UUID id = UUID.randomUUID();
        Account account = new Account(
                id,
                Calendar.getInstance(),
                new ArrayList<>(),
                new Money(0.0),
                AccountStatus.ACTIVE
        );

        accountRepository.addAccount(account);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            accountRepository.addAccount(account);
        });

        assertEquals("Account with this id " + id + " already exists", exception.getMessage());
    }

    @Test
    void testUpdateBalance_NonExistentAccount_ThrowsException() {
        UUID id = UUID.randomUUID();
        Account account = new Account(
                id,
                Calendar.getInstance(),
                new ArrayList<>(),
                new Money(0.0),
                AccountStatus.ACTIVE
        );

        Exception exception = assertThrows(RuntimeException.class, () -> {
            accountRepository.updateBalance(account, new Money(100.0));
        });

        assertEquals("No such account in repository", exception.getMessage());
    }

    @Test
    void testRegisterTransaction_WithExistingId_ThrowsException() {
        UUID accountId = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();

        Account account = new Account(
                accountId,
                Calendar.getInstance(),
                new ArrayList<>(),
                new Money(100.0),
                AccountStatus.ACTIVE
        );

        Transaction mockTransaction = new Zmxcrr.models.transactions.DepositTransaction(
                transactionId,
                Calendar.getInstance(),
                TransactionStatus.SUCCEED,
                account,
                new Money(50.0)
        );

        transactionRepository.registerTransaction(mockTransaction);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            transactionRepository.registerTransaction(mockTransaction);
        });

        assertEquals("Transaction with this ID " + transactionId + " exists", exception.getMessage());
    }

    @Test
    void testUpdateStatus_NonExistentTransaction_ThrowsException() {
        UUID accountId = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();

        Account account = new Account(
                accountId,
                Calendar.getInstance(),
                new ArrayList<>(),
                new Money(100.0),
                AccountStatus.ACTIVE
        );

        Transaction mockTransaction = new Zmxcrr.models.transactions.DepositTransaction(
                transactionId,
                Calendar.getInstance(),
                TransactionStatus.SUCCEED,
                account,
                new Money(50.0)
        );

        Exception exception = assertThrows(RuntimeException.class, () -> {
            transactionRepository.updateStatus(mockTransaction, TransactionStatus.FAILED);
        });

        assertEquals("No such transaction in repository", exception.getMessage());
    }

    @Test
    void testAddUser_WithExistingId_ThrowsException() {
        UUID userId = UUID.randomUUID();
        User user = new User(
                userId,
                "Test User",
                new PinCode("1234"),
                new ArrayList<>()
        );

        userRepository.addUser(user);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userRepository.addUser(user);
        });

        assertEquals("Account with this id " + userId + " already exists", exception.getMessage());
    }

    @Test
    void testUpdateUserPinCode_NonExistentUser_ThrowsException() {
        UUID userId = UUID.randomUUID();
        PinCode pinCode = new PinCode("5678");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userRepository.updateUserPinCode(userId, pinCode);
        });

        assertEquals("User with id " + userId + " not found", exception.getMessage());
    }

    @Test
    void testChangeUserPinCode_NonExistentUser_ThrowsException() {
        UUID nonExistentUserId = UUID.randomUUID();
        PinCode oldPin = new PinCode("1234");
        PinCode newPin = new PinCode("5678");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.changeUserPinCode(nonExistentUserId, oldPin, newPin);
        });

        assertEquals("Wrong PinCode or UserId", exception.getMessage());
    }

    @Test
    void testWithdraw_InsufficientFunds_ReturnsFailResult() {
        User user = userService.createUser("Test User", new PinCode("1234"));
        Account account = accountService.createAccount(user);

        accountOperationService.deposit(account, 50.0);

        ServiceOperationResult result = accountOperationService.withdraw(account, 100.0);

        assertTrue(result instanceof ServiceOperationResult.Fail);
        assertEquals("Can't withdraw more than have (Value: 100.0, Balance: 50.0)",
                ((ServiceOperationResult.Fail) result).getMessage());
    }

    @Test
    void testDeposit_SuspendedAccount_ReturnsFailResult() {
        User user = userService.createUser("Test User", new PinCode("1234"));
        Account account = accountService.createAccount(user);
        account.setStatus(AccountStatus.SUSPENDED);

        ServiceOperationResult result = accountOperationService.deposit(account, 100.0);

        assertTrue(result instanceof ServiceOperationResult.Fail);
        assertEquals("Your account has been suspended",
                ((ServiceOperationResult.Fail) result).getMessage());
    }
}