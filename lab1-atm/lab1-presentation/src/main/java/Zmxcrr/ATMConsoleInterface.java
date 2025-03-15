package Zmxcrr;

import Zmxcrr.contracts.*;
import Zmxcrr.models.*;
import Zmxcrr.models.accounts.*;
import Zmxcrr.models.transactions.*;

import java.util.*;

public class ATMConsoleInterface {
    private final Scanner scanner;
    private final AccountService accountService;
    private final UserService userService;
    private final AccountOperationService operationService;
    private final TransactionService transactionService;
    private User currentUser;

    public ATMConsoleInterface(
            AccountService accountService,
            UserService userService,
            AccountOperationService operationService,
            TransactionService transactionService) {
        this.accountService = accountService;
        this.userService = userService;
        this.operationService = operationService;
        this.transactionService = transactionService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }
    private void showLoginMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("1. Log in");
        System.out.println("2. Sign up");
        System.out.println("0. Exit");
        System.out.print("Choose action: ");

        int choice = readIntInput();

        switch (choice) {
            case 1 -> login();
            case 2 -> register();
            case 0 -> {
                System.exit(0);
            }
            default -> System.out.println("Wrong choice. Try again.");
        }
    }

    private void showMainMenu() {
        System.out.println("\n=== Main menu ===");
        System.out.println("1. View account list");
        System.out.println("2. Create New Account");
        System.out.println("3. Deposit");
        System.out.println("4. Withdraw");
        System.out.println("5. View transaction history");
        System.out.println("6. Change pinCode");
        System.out.println("7. Log out");
        System.out.println("0. Exit");
        System.out.print("Choose action: ");

        int choice = readIntInput();

        switch (choice) {
            case 1 -> viewAccounts();
            case 2 -> createAccount();
            case 3 -> deposit();
            case 4 -> withdraw();
            case 5 -> viewTransactionHistory();
            case 6 -> changePinCode();
            case 7 -> logout();
            case 0 -> {
                System.exit(0);
            }
            default -> System.out.println("Wrong choice. Try again.");
        }
    }

    private void login() {
        try {
            System.out.print("Enter userId: ");
            String userIdStr = scanner.nextLine().trim();
            UUID userId = UUID.fromString(userIdStr);

            System.out.print("Enter pinCode (4 digits): ");
            String pin = scanner.nextLine().trim();
            PinCode pinCode = new PinCode(pin);

            Optional<User> userOpt = userService.getUserById(userId);
            if (userOpt.isPresent() && userService.authenticateUser(userId, pinCode)) {
                currentUser = userOpt.get();
                System.out.println("Login successful.");
            } else {
                System.out.println("Wrong userId or pinCode");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
        }
    }

    private void register() {
        try {
            System.out.print("Enter your name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter pinCode (4 digits): ");
            String pin = scanner.nextLine().trim();
            PinCode pinCode = new PinCode(pin);

            User newUser = userService.createUser(name, pinCode);
            System.out.println("Successfully registered.");
            System.out.println("Your id: " + newUser.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Registration error " + e.getMessage());
        }
    }

    private void viewAccounts() {
        List<Account> accounts = accountService.getAccountsByUser(currentUser);

        if (accounts.isEmpty()) {
            System.out.println("You don't have accounts.");
            return;
        }

        System.out.println("\n=== Your accounts ===");
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            System.out.println((i + 1) + ". ID: " + account.getId()
                    + ", Balance: " + account.getBalance().getValue()
                    + ", Status: " + account.getStatus());
        }
    }

    private void createAccount() {
        try {
            Account newAccount = accountService.createAccount(currentUser);
            System.out.println("Account created successfully.");
            System.out.println("Account id: " + newAccount.getId());
        } catch (Exception e) {
            System.out.println("Error while creating account: " + e.getMessage());
        }
    }

    private void deposit() {
        try {
            Account account = selectAccount();
            if (account == null) return;

            System.out.print("Enter amount to deposit: ");
            double amount = readDoubleInput();

            if (amount <= 0) {
                System.out.println("Amount must be greater than 0.");
                return;
            }

            ServiceOperationResult result = operationService.deposit(account, amount);

            if (result instanceof ServiceOperationResult.Success) {
                System.out.println("Deposit successful.");
                System.out.println("New Balance: " + account.getBalance().getValue());
            } else if (result instanceof ServiceOperationResult.Fail) {
                System.out.println("Error while trying to deposit money: " + ((ServiceOperationResult.Fail) result).getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void withdraw() {
        try {
            Account account = selectAccount();
            if (account == null) return;

            System.out.print("Enter amount to withdraw: ");
            double amount = readDoubleInput();

            if (amount <= 0) {
                System.out.println("Amount must be greater than 0.");
                return;
            }

            ServiceOperationResult result = operationService.withdraw(account, amount);

            if (result instanceof ServiceOperationResult.Success) {
                System.out.println("Withdraw successful.");
                System.out.println("New balance: " + account.getBalance().getValue());
            } else if (result instanceof ServiceOperationResult.Fail) {
                System.out.println("Error while trying to withdraw: " + ((ServiceOperationResult.Fail) result).getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewTransactionHistory() {
        try {
            Account account = selectAccount();
            if (account == null) return;

            List<Transaction> transactions = transactionService.getAccountTransactions(account);

            if (transactions.isEmpty()) {
                System.out.println("No operations by this account");
                return;
            }

            System.out.println("\n=== Account transaction history " + account.getId() + " ===");
            for (Transaction transaction : transactions) {
                System.out.println("------------------------------");
                System.out.println(transaction.toString());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void changePinCode() {
        try {
            System.out.print("Enter your current pinCode: ");
            String currentPin = scanner.nextLine().trim();
            PinCode oldPinCode = new PinCode(currentPin);

            System.out.print("Enter new pinCode (4 digits): ");
            String newPin = scanner.nextLine().trim();
            PinCode newPinCode = new PinCode(newPin);

            userService.changeUserPinCode(currentUser.getId(), oldPinCode, newPinCode);
            System.out.println("Pincode changed successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Error while trying to change pinCode " + e.getMessage());
        }
    }

    private void logout() {
        currentUser = null;
        System.out.println("Log out successful.");
    }

    private Account selectAccount() {
        List<Account> accounts = accountService.getAccountsByUser(currentUser);

        if (accounts.isEmpty()) {
            System.out.println("You don't have accounts.");
            return null;
        }

        System.out.println("\nChoose your account:");
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            System.out.println((i + 1) + ". ID: " + account.getId()
                    + ", Balance: " + account.getBalance().getValue()
                    + ", Status: " + account.getStatus());
        }

        System.out.print("Enter account number (1-" + accounts.size() + "): ");
        int choice = readIntInput();

        if (choice < 1 || choice > accounts.size()) {
            System.out.println("Wrong choice.");
            return null;
        }

        return accounts.get(choice - 1);
    }

    private int readIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private double readDoubleInput() {
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}