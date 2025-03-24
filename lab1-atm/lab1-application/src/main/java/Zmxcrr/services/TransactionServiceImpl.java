package Zmxcrr.services;

import Zmxcrr.contracts.TransactionService;
import Zmxcrr.infrastructure.TransactionRepository;
import Zmxcrr.models.Money;
import Zmxcrr.models.accounts.Account;
import Zmxcrr.models.transactions.DepositTransaction;
import Zmxcrr.models.transactions.Transaction;
import Zmxcrr.models.transactions.TransactionStatus;
import Zmxcrr.models.transactions.WithdrawTransaction;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    @Override
    public void createDepositTransaction(Calendar date, Account account, Money value, TransactionStatus status) {
        var transaction = new DepositTransaction(
                UUID.randomUUID(),
                Calendar.getInstance(),
                status,
                account,
                new Money(value.getValue())
        );

        account.getTransactions().add(transaction);
        transactionRepository.registerTransaction(transaction);
    }

    @Override
    public void createWithdrawTransaction(Calendar date, Account account, Money value, TransactionStatus status) {
        var transaction = new WithdrawTransaction(
                UUID.randomUUID(),
                Calendar.getInstance(),
                status,
                account,
                new Money(value.getValue())
        );

        account.getTransactions().add(transaction);
        transactionRepository.registerTransaction(transaction);
    }

    @Override
    public List<Transaction> getAccountTransactions(Account account) {
        return account.getTransactions();
    }

    @Override
    public Optional<Transaction> getTransactionById(UUID id) {
        return transactionRepository.getTransactionById(id);
    }
}
