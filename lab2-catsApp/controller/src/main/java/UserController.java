import dto.UserDto;
import entities.Owner;
import entities.User;
import enums.UserRole;
import services.OwnerService;
import services.UserService;

import java.util.List;
import java.util.Scanner;

public class UserController {
    private final UserService userService;
    private final OwnerService ownerService;
    private final Scanner scanner;

    public UserController(UserService userService, OwnerService ownerService, Scanner scanner) {
        this.userService = userService;
        this.ownerService = ownerService;
        this.scanner = scanner;
    }

    public void showMainMenu() {
        while (true) {
            System.out.println("\n===== User Management =====");
            System.out.println("1. List all users");
            System.out.println("2. Find user by username");
            System.out.println("3. Create new user");
            System.out.println("4. Update user");
            System.out.println("5. Delete user");
            System.out.println("0. Back to main menu");

            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    listAllUsers();
                    break;
                case 2:
                    findUserByUsername();
                    break;
                case 3:
                    createUser();
                    break;
                case 4:
                    updateUser();
                    break;
                case 5:
                    deleteUser();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void listAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        System.out.println("\n===== All Users =====");
        for (User user : users) {
            displayUser(user);
        }
    }

    private void findUserByUsername() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        User user = userService.getUser(username);
        if (user == null) {
            System.out.println("User not found with username: " + username);
            return;
        }

        System.out.println("\n===== User Details =====");
        displayUser(user);
    }

    private void createUser() {
        UserDto userDto = new UserDto();

        System.out.println("\n===== Create New User =====");
        System.out.print("Username: ");
        userDto.setUsername(scanner.nextLine());

        System.out.print("Password: ");
        userDto.setPassword(scanner.nextLine());

        System.out.println("Available roles: ");
        for (UserRole role : UserRole.values()) {
            System.out.println("- " + role);
        }
        System.out.print("Role: ");
        userDto.setRole(UserRole.valueOf(scanner.nextLine().toUpperCase()));

        System.out.print("Link to Owner ID (leave empty if none): ");
        String ownerIdStr = scanner.nextLine();
        if (!ownerIdStr.isEmpty()) {
            long ownerId = Long.parseLong(ownerIdStr);
            Owner owner = ownerService.getOwner(ownerId);
            if (owner == null) {
                System.out.println("Owner not found with ID: " + ownerId);
                return;
            }
            userDto.setOwner(ownerId);
        }

        try {
            User createdUser = userService.createUser(userDto);
            System.out.println("User created successfully: " + createdUser.getUsername());
        } catch (Exception e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
    }

    private void updateUser() {
        System.out.print("Enter username to update: ");
        String username = scanner.nextLine();

        User existingUser = userService.getUser(username);
        if (existingUser == null) {
            System.out.println("User not found with username: " + username);
            return;
        }

        UserDto userDto = new UserDto(existingUser);

        System.out.println("\n===== Update User =====");
        System.out.println("Leave field empty to keep current value");

        System.out.print("Password (leave empty to keep current): ");
        String password = scanner.nextLine();
        if (!password.isEmpty()) {
            userDto.setPassword(password);
        }

        System.out.println("Available roles: ");
        for (UserRole role : UserRole.values()) {
            System.out.println("- " + role);
        }
        System.out.print("Role [" + existingUser.getRole() + "]: ");
        String roleStr = scanner.nextLine();
        if (!roleStr.isEmpty()) {
            userDto.setRole(UserRole.valueOf(roleStr.toUpperCase()));
        }

        System.out.print("Link to Owner ID [" + (existingUser.getOwner() != null ? existingUser.getOwner().getId() : "none") +
                "] (enter 'none' to remove link, or leave empty to keep current): ");
        String ownerIdStr = scanner.nextLine();
        if (!ownerIdStr.isEmpty()) {
            if (ownerIdStr.equalsIgnoreCase("none")) {
                userDto.setOwner(null);
            } else {
                long ownerId = Long.parseLong(ownerIdStr);
                Owner owner = ownerService.getOwner(ownerId);
                if (owner == null) {
                    System.out.println("Owner not found with ID: " + ownerId);
                    return;
                }
                userDto.setOwner(ownerId);
            }
        }

        try {
            User updatedUser = userService.updateUser(userDto);
            System.out.println("User updated successfully.");
        } catch (Exception e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
    }

    private void deleteUser() {
        System.out.print("Enter username to delete: ");
        String username = scanner.nextLine();

        User user = userService.getUser(username);
        if (user == null) {
            System.out.println("User not found with username: " + username);
            return;
        }

        System.out.print("Are you sure you want to delete this user? (y/n): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            try {
                userService.deleteUser(username);
                System.out.println("User deleted successfully.");
            } catch (Exception e) {
                System.out.println("Error deleting user: " + e.getMessage());
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private void displayUser(User user) {
        System.out.println("Username: " + user.getUsername());
        System.out.println("Role: " + user.getRole());
        System.out.println("Linked Owner: " + (user.getOwner() != null ?
                user.getOwner().getFirstName() + " " + user.getOwner().getLastName() + " (ID: " + user.getOwner().getId() + ")" :
                "None"));
        System.out.println("------------------------------");
    }
}