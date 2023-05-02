package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {

        String jdbcURL = "jdbc:mysql://localhost:3306/library";
        String user;
        String pass;

        Scanner scanner = new Scanner(System.in);

        Connection connection;

        try {

            System.out.println("Welcome to Library!");
            System.out.println("Enter username: ");
            user = scanner.nextLine();
            System.out.println("Enter password to your library: ");
            pass = scanner.nextLine();

            connection = DriverManager.getConnection(jdbcURL, user, pass);

            DBController dbController = new DBController(connection);
            Validator validator = new Validator();
            LibraryController libraryController = new LibraryController(validator, dbController);

            int choice;

            while (!connection.isClosed()) {

                System.out.println("""
                        1. Show all books
                        2. Borrow a book
                        3. Return a book
                        4. Check book status
                        5. Add a book
                        6. Delete a book
                        7. Close Library
                        Choose your action:
                        """
                );

                choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1 -> libraryController.showAllBooks();
                    case 2 -> libraryController.borrowBook();
                    case 3 -> libraryController.returnBook();
                    case 4 -> libraryController.checkBookStatus();
                    case 5 -> libraryController.addBook();
                    case 6 -> libraryController.deleteBook();
                    case 7 -> {
                        connection.close();
                        System.exit(0);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}