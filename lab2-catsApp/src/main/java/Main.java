import interfaces.CatDao;
import interfaces.OwnerDao;
import interfaces.UserDao;
import org.hibernate.SessionFactory;
import services.CatService;
import services.OwnerService;
import services.UserService;
import utils.FlywayMigrationInitializer;
import utils.HibernateUtil;

import java.util.Scanner;

public class Main {
    private static SessionFactory sessionFactory;
    private static Scanner scanner;

    private static CatDao catDao;
    private static OwnerDao ownerDao;
    private static UserDao userDao;

    private static CatService catService;
    private static OwnerService ownerService;
    private static UserService userService;

    private static CatController catController;
    private static OwnerController ownerController;
    private static UserController userController;

    public static void main(String[] args) {
        try {
            FlywayMigrationInitializer.initializeFlyway();

            initializeResources();
            showMainMenu();
        } catch (Exception e) {
            System.err.println("Application error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    private static void initializeResources() {
        sessionFactory = HibernateUtil.getSessionFactory();

        scanner = new Scanner(System.in);

        catDao = new CatDaoImpl(sessionFactory);
        ownerDao = new OwnerDaoImpl(sessionFactory);
        userDao = new UserDaoImpl(sessionFactory);

        catService = new CatService(catDao, ownerDao);
        ownerService = new OwnerService(ownerDao);
        userService = new UserService(userDao, ownerDao);

        catController = new CatController(catService, scanner);
        ownerController = new OwnerController(ownerService, catService, scanner);
        userController = new UserController(userService, ownerService, scanner);
    }

    private static void closeResources() {
        if (scanner != null) {
            scanner.close();
        }

        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("\n===== Pet Management System =====");
            System.out.println("1. Cat Management");
            System.out.println("2. Owner Management");
            System.out.println("3. User Management");
            System.out.println("0. Exit");

            System.out.print("Enter your choice: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    catController.showMainMenu();
                    break;
                case 2:
                    ownerController.showMainMenu();
                    break;
                case 3:
                    userController.showMainMenu();
                    break;
                case 0:
                    System.out.println("Exiting application. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
