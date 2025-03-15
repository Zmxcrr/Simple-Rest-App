package Zmxcrr.models.accounts;

import Zmxcrr.models.Money;
import Zmxcrr.models.transactions.Transaction;
import lombok.*;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class Account {
    @EqualsAndHashCode.Include
    protected final UUID id;
    @ToString.Exclude
    protected final Calendar creationDate;
    @ToString.Exclude
    protected final List<Transaction> transactions;
    protected Money balance;
    protected AccountStatus status;

    public AccountOperationResult deposit(Money value) {
        var current = balance.getValue();
        balance.setValue(current + value.getValue());

        return new AccountOperationResult.Success(this);
    }
    public AccountOperationResult withdraw(Money value) {
        var current = balance.getValue();

        if (value.getValue() > current)
            return new AccountOperationResult.Fail("Can't withdraw more than have (Value: %s, Balance: %s)".formatted(value.getValue(), current));

        balance.setValue(current - value.getValue());

        return new AccountOperationResult.Success(this);
    }
}