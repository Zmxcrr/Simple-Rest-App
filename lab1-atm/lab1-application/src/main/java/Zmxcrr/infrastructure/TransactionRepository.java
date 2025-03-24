package Zmxcrr.infrastructure;

import Zmxcrr.models.transactions.Transaction;
import Zmxcrr.models.transactions.TransactionStatus;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {
    void registerTransaction(Transaction transaction);
    Optional<Transaction> getTransactionById(UUID id);
    void updateStatus(Transaction transaction, TransactionStatus status);
}
