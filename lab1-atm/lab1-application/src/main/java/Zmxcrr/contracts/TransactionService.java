package Zmxcrr.contracts;

import Zmxcrr.models.Money;
import Zmxcrr.models.accounts.Account;
import Zmxcrr.models.transactions.Transaction;
import Zmxcrr.models.transactions.TransactionStatus;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionService {
    void createDepositTransaction(Calendar date, Account account, Money value, TransactionStatus status);
    void createWithdrawTransaction(Calendar date, Account account, Money value, TransactionStatus status);
    List<Transaction> getAccountTransactions(Account account);
    Optional<Transaction> getTransactionById(UUID id);
}