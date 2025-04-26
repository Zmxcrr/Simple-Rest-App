import dto.OwnerDto;
import entities.Cat;
import entities.Owner;
import services.CatService;
import services.OwnerService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class OwnerController {
    private final OwnerService ownerService;
    private final CatService catService;
    private final Scanner scanner;

    public OwnerController(OwnerService ownerService, CatService catService, Scanner scanner) {
        this.ownerService = ownerService;
        this.catService = catService;
        this.scanner = scanner;
    }

    public void showMainMenu() {
        while (true) {
            System.out.println("\n===== Owner Management =====");
            System.out.println("1. List all owners");
            System.out.println("2. Find owner by ID");
            System.out.println("3. Create new owner");
            System.out.println("4. Update owner");
            System.out.println("5. Delete owner");
            System.out.println("6. List owner's cats");
            System.out.println("0. Back to main menu");

            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    listAllOwners();
                    break;
                case 2:
                    findOwnerById();
                    break;
                case 3:
                    createOwner();
                    break;
                case 4:
                    updateOwner();
                    break;
                case 5:
                    deleteOwner();
                    break;
                case 6:
                    listOwnerCats();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void listAllOwners() {
        List<Owner> owners = ownerService.getAllOwners();
        if (owners.isEmpty()) {
            System.out.println("No owners found.");
            return;
        }

        System.out.println("\n===== All Owners =====");
        for (Owner owner : owners) {
            displayOwner(owner);
        }
    }

    private void findOwnerById() {
        System.out.print("Enter owner ID: ");
        long id = Long.parseLong(scanner.nextLine());

        Owner owner = ownerService.getOwner(id);
        if (owner == null) {
            System.out.println("Owner not found with ID: " + id);
            return;
        }

        System.out.println("\n===== Owner Details =====");
        displayOwner(owner);
    }

    private void createOwner() {
        OwnerDto ownerDto = new OwnerDto();

        System.out.println("\n===== Create New Owner =====");
        System.out.print("First Name: ");
        ownerDto.setFirstName(scanner.nextLine());

        System.out.print("Last Name: ");
        ownerDto.setLastName(scanner.nextLine());

        System.out.print("Birthdate (YYYY-MM-DD): ");
        String birthdateStr = scanner.nextLine();
        ownerDto.setBirthdate(LocalDate.parse(birthdateStr));

        try {
            Owner createdOwner = ownerService.createOwner(ownerDto);
            System.out.println("Owner created successfully with ID: " + createdOwner.getId());
        } catch (Exception e) {
            System.out.println("Error creating owner: " + e.getMessage());
        }
    }

    private void updateOwner() {
        System.out.print("Enter owner ID to update: ");
        long id = Long.parseLong(scanner.nextLine());

        Owner existingOwner = ownerService.getOwner(id);
        if (existingOwner == null) {
            System.out.println("Owner not found with ID: " + id);
            return;
        }

        OwnerDto ownerDto = new OwnerDto(existingOwner);

        System.out.println("\n===== Update Owner =====");
        System.out.println("Leave field empty to keep current value");

        System.out.print("First Name [" + existingOwner.getFirstName() + "]: ");
        String firstName = scanner.nextLine();
        if (!firstName.isEmpty()) {
            ownerDto.setFirstName(firstName);
        }

        System.out.print("Last Name [" + existingOwner.getLastName() + "]: ");
        String lastName = scanner.nextLine();
        if (!lastName.isEmpty()) {
            ownerDto.setLastName(lastName);
        }

        System.out.print("Birthdate [" + existingOwner.getBirthdate() + "] (YYYY-MM-DD): ");
        String birthdateStr = scanner.nextLine();
        if (!birthdateStr.isEmpty()) {
            ownerDto.setBirthdate(LocalDate.parse(birthdateStr));
        }

        try {
            Owner updatedOwner = ownerService.updateOwner(ownerDto);
            System.out.println("Owner updated successfully.");
        } catch (Exception e) {
            System.out.println("Error updating owner: " + e.getMessage());
        }
    }

    private void deleteOwner() {
        System.out.print("Enter owner ID to delete: ");
        long id = Long.parseLong(scanner.nextLine());

        Owner owner = ownerService.getOwner(id);
        if (owner == null) {
            System.out.println("Owner not found with ID: " + id);
            return;
        }

        System.out.print("Are you sure you want to delete this owner? This will affect their cats. (y/n): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            try {
                ownerService.deleteOwner(id);
                System.out.println("Owner deleted successfully.");
            } catch (Exception e) {
                System.out.println("Error deleting owner: " + e.getMessage());
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private void listOwnerCats() {
        System.out.print("Enter owner ID: ");
        long id = Long.parseLong(scanner.nextLine());

        Owner owner = ownerService.getOwner(id);
        if (owner == null) {
            System.out.println("Owner not found with ID: " + id);
            return;
        }

        List<Cat> cats = catService.getCatsByOwnerId(id);
        if (cats.isEmpty()) {
            System.out.println("This owner has no cats.");
            return;
        }

        System.out.println("\n===== Cats of " + owner.getFirstName() + " " + owner.getLastName() + " =====");
        for (Cat cat : cats) {
            System.out.println("ID: " + cat.getId());
            System.out.println("Name: " + cat.getName());
            System.out.println("Breed: " + cat.getBreed());
            System.out.println("------------------------------");
        }
    }

    private void displayOwner(Owner owner) {
        System.out.println("ID: " + owner.getId());
        System.out.println("Name: " + owner.getFirstName() + " " + owner.getLastName());
        System.out.println("Birthdate: " + owner.getBirthdate());
        System.out.println("Number of cats: " + owner.getCats().size());
        System.out.println("------------------------------");
    }
}