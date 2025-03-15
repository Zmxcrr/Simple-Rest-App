package Zmxcrr.services;

import Zmxcrr.contracts.AccountOperationService;
import Zmxcrr.contracts.ServiceOperationResult;
import Zmxcrr.contracts.TransactionService;
import Zmxcrr.infrastructure.AccountRepository;
import Zmxcrr.models.Money;
import Zmxcrr.models.accounts.Account;
import Zmxcrr.models.accounts.AccountOperationResult;
import Zmxcrr.models.accounts.AccountStatus;
import Zmxcrr.models.transactions.TransactionStatus;

import java.util.Calendar;

public class AccountOperationServiceImpl implements AccountOperationService {
    private final AccountRepository accountRepository;
    private final TransactionService transactionService;
    public AccountOperationServiceImpl(AccountRepository accountRepository, TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
    }

    @Override
    public ServiceOperationResult deposit(Account account, double value) {
        if (account.getStatus().equals(AccountStatus.SUSPENDED)) {
            return new ServiceOperationResult.Fail("Your account has been suspended");
        }

        var moneyToDeposit = new Money(value);
        var result = account.deposit(moneyToDeposit);

        if (result instanceof AccountOperationResult.Success success) {
            accountRepository.updateBalance(success.getAccount(), success.getAccount().getBalance());
            transactionService.createDepositTransaction(
                    Calendar.getInstance(),
                    account,
                    moneyToDeposit,
                    TransactionStatus.SUCCEED);

            return new ServiceOperationResult.Success();
        }

        transactionService.createDepositTransaction(
                Calendar.getInstance(),
                account,
                moneyToDeposit,
                TransactionStatus.FAILED);

        return new ServiceOperationResult.Fail(((AccountOperationResult.Fail) result).getMessage());
    }

    @Override
    public ServiceOperationResult withdraw(Account account, double value) {
        if (account.getStatus().equals(AccountStatus.SUSPENDED)) {
            return new ServiceOperationResult.Fail("Your account has been suspended");
        }

        var moneyToWithdraw = new Money(value);
        var result = account.withdraw(moneyToWithdraw);

        if (result instanceof AccountOperationResult.Success success) {
            accountRepository.updateBalance(success.getAccount(), success.getAccount().getBalance());
            transactionService.createWithdrawTransaction(
                    Calendar.getInstance(),
                    account,
                    moneyToWithdraw,
                    TransactionStatus.SUCCEED);

            return new ServiceOperationResult.Success();
        }

        transactionService.createWithdrawTransaction(
                Calendar.getInstance(),
                account,
                moneyToWithdraw,
                TransactionStatus.FAILED);

        return new ServiceOperationResult.Fail(((AccountOperationResult.Fail) result).getMessage());
    }
}
