import dto.CatDto;
import entities.Cat;
import enums.CatColor;
import services.CatService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CatController {
    private final CatService catService;
    private final Scanner scanner;

    public CatController(CatService catService, Scanner scanner) {
        this.catService = catService;
        this.scanner = scanner;
    }

    public void showMainMenu() {
        while (true) {
            System.out.println("\n===== Cat Management =====");
            System.out.println("1. List all cats");
            System.out.println("2. Find cat by ID");
            System.out.println("3. Create new cat");
            System.out.println("4. Update cat");
            System.out.println("5. Delete cat");
            System.out.println("0. Back to main menu");

            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    listAllCats();
                    break;
                case 2:
                    findCatById();
                    break;
                case 3:
                    createCat();
                    break;
                case 4:
                    updateCat();
                    break;
                case 5:
                    deleteCat();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void listAllCats() {
        List<Cat> cats = catService.getAllCats();
        if (cats.isEmpty()) {
            System.out.println("No cats found.");
            return;
        }

        System.out.println("\n===== All Cats =====");
        for (Cat cat : cats) {
            displayCat(cat);
        }
    }

    private void findCatById() {
        System.out.print("Enter cat ID: ");
        long id = Long.parseLong(scanner.nextLine());

        Cat cat = catService.getCat(id);
        if (cat == null) {
            System.out.println("Cat not found with ID: " + id);
            return;
        }

        System.out.println("\n===== Cat Details =====");
        displayCat(cat);
    }

    private void createCat() {
        CatDto catDto = new CatDto();

        System.out.println("\n===== Create New Cat =====");
        System.out.print("Name: ");
        catDto.setName(scanner.nextLine());

        System.out.print("Birthdate (YYYY-MM-DD): ");
        String birthdateStr = scanner.nextLine();
        catDto.setBirthdate(LocalDate.parse(birthdateStr));

        System.out.print("Breed: ");
        catDto.setBreed(scanner.nextLine());

        System.out.println("Available colors: ");
        for (CatColor color : CatColor.values()) {
            System.out.println("- " + color);
        }
        System.out.print("Color: ");
        catDto.setColor(CatColor.valueOf(scanner.nextLine().toUpperCase()));

        System.out.print("Owner ID (leave empty if none): ");
        String ownerIdStr = scanner.nextLine();
        if (!ownerIdStr.isEmpty()) {
            catDto.setOwner(Long.parseLong(ownerIdStr));
        }

        System.out.print("Friend IDs (comma-separated, leave empty if none): ");
        String friendIdsStr = scanner.nextLine();
        if (!friendIdsStr.isEmpty()) {
            List<Long> friendIds = Arrays.stream(friendIdsStr.split(","))
                    .map(String::trim)
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            catDto.setFriends(friendIds);
        } else {
            catDto.setFriends(new ArrayList<>());
        }

        try {
            Cat createdCat = catService.createCat(catDto);
            System.out.println("Cat created successfully with ID: " + createdCat.getId());
        } catch (Exception e) {
            System.out.println("Error creating cat: " + e.getMessage());
        }
    }

    private void updateCat() {
        System.out.print("Enter cat ID to update: ");
        long id = Long.parseLong(scanner.nextLine());

        Cat existingCat = catService.getCat(id);
        if (existingCat == null) {
            System.out.println("Cat not found with ID: " + id);
            return;
        }

        CatDto catDto = new CatDto(existingCat);

        System.out.println("\n===== Update Cat =====");
        System.out.println("Leave field empty to keep current value");

        System.out.print("Name [" + existingCat.getName() + "]: ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            catDto.setName(name);
        }

        System.out.print("Birthdate [" + existingCat.getBirthdate() + "] (YYYY-MM-DD): ");
        String birthdateStr = scanner.nextLine();
        if (!birthdateStr.isEmpty()) {
            catDto.setBirthdate(LocalDate.parse(birthdateStr));
        }

        System.out.print("Breed [" + existingCat.getBreed() + "]: ");
        String breed = scanner.nextLine();
        if (!breed.isEmpty()) {
            catDto.setBreed(breed);
        }

        System.out.println("Available colors: ");
        for (CatColor color : CatColor.values()) {
            System.out.println("- " + color);
        }
        System.out.print("Color [" + existingCat.getColor() + "]: ");
        String colorStr = scanner.nextLine();
        if (!colorStr.isEmpty()) {
            catDto.setColor(CatColor.valueOf(colorStr.toUpperCase()));
        }

        System.out.print("Owner ID [" + (existingCat.getOwner() != null ? existingCat.getOwner().getId() : "none") + "] (leave empty to keep current): ");
        String ownerIdStr = scanner.nextLine();
        if (!ownerIdStr.isEmpty()) {
            if (ownerIdStr.equalsIgnoreCase("none")) {
                catDto.setOwner(null);
            } else {
                catDto.setOwner(Long.parseLong(ownerIdStr));
            }
        }

        System.out.print("Friend IDs [" +
                existingCat.getFriends().stream()
                        .map(Cat::getId)
                        .map(String::valueOf)
                        .collect(Collectors.joining(", ")) +
                "] (comma-separated, leave empty to keep current): ");
        String friendIdsStr = scanner.nextLine();
        if (!friendIdsStr.isEmpty()) {
            if (friendIdsStr.equalsIgnoreCase("none")) {
                catDto.setFriends(new ArrayList<>());
            } else {
                List<Long> friendIds = Arrays.stream(friendIdsStr.split(","))
                        .map(String::trim)
                        .map(Long::parseLong)
                        .collect(Collectors.toList());
                catDto.setFriends(friendIds);
            }
        }

        try {
            Cat updatedCat = catService.updateCat(catDto);
            System.out.println("Cat updated successfully.");
        } catch (Exception e) {
            System.out.println("Error updating cat: " + e.getMessage());
        }
    }

    private void deleteCat() {
        System.out.print("Enter cat ID to delete: ");
        long id = Long.parseLong(scanner.nextLine());

        Cat cat = catService.getCat(id);
        if (cat == null) {
            System.out.println("Cat not found with ID: " + id);
            return;
        }

        System.out.print("Are you sure you want to delete this cat? (y/n): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            try {
                catService.deleteCat(id);
                System.out.println("Cat deleted successfully.");
            } catch (Exception e) {
                System.out.println("Error deleting cat: " + e.getMessage());
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private void displayCat(Cat cat) {
        System.out.println("ID: " + cat.getId());
        System.out.println("Name: " + cat.getName());
        System.out.println("Birthdate: " + cat.getBirthdate());
        System.out.println("Breed: " + cat.getBreed());
        System.out.println("Color: " + cat.getColor());
        System.out.println("Owner: " + (cat.getOwner() != null ?
                cat.getOwner().getFirstName() + " " + cat.getOwner().getLastName() + " (ID: " + cat.getOwner().getId() + ")" :
                "None"));

        System.out.println("Friends: " +
                (cat.getFriends() != null && !cat.getFriends().isEmpty() ?
                        cat.getFriends().stream()
                                .map(friend -> friend.getName() + " (ID: " + friend.getId() + ")")
                                .collect(Collectors.joining(", ")) :
                        "None"));
        System.out.println("------------------------------");
    }
}