package org.example;

import com.diogonunes.jcolor.Ansi;
import com.diogonunes.jcolor.Attribute;

import java.util.Scanner;

public class LibraryController {


    private final Validator validator;
    private final DBController dbController;

    private final Scanner scanner;

    public LibraryController(Validator validator, DBController dbController) {
        this.validator = validator;
        this.dbController = dbController;
        scanner = new Scanner(System.in);
    }


    public void addBook() {

        String authorName;
        String bookTitle;
        String releaseYear;

        System.out.println("Enter author: ");
        authorName = scanner.nextLine();
        while (!validator.validateAuthor(authorName)) {
            System.out.println(Ansi.colorize("Error in author name. Try again: ", Attribute.RED_TEXT()));
            authorName = scanner.nextLine();
        }

        System.out.println("Enter title: ");
        bookTitle = scanner.nextLine();
        while (!validator.validateTitle(bookTitle)) {
            System.out.println(Ansi.colorize("Error in title. Try again: ", Attribute.RED_TEXT()));
            bookTitle = scanner.nextLine();
        }

        System.out.println("Enter release year: ");
        releaseYear = scanner.nextLine();
        while (!validator.validateReleaseYear(releaseYear)) {
            System.out.println(Ansi.colorize("Wrong release year. Try again: ", Attribute.RED_TEXT()));
            releaseYear = scanner.nextLine();
        }

        dbController.addBookToDb(authorName, bookTitle, releaseYear, true);

    }

    public void deleteBook() {

        String titleToDelete;

        System.out.println("Enter title to delete: ");
        titleToDelete = scanner.nextLine();
        while (!validator.validateAuthor(titleToDelete)) {
            System.out.println(Ansi.colorize("You entered a wrong title name. Try again: ", Attribute.RED_TEXT()));
            titleToDelete = scanner.nextLine();
        }

        dbController.deleteBookFromDb(titleToDelete);

    }

    public void showAllBooks() {

        System.out.println(Ansi.colorize("| ID | AUTHOR | TITLE | RELEASE YEAR | AVAILABLE |", Attribute.BRIGHT_CYAN_TEXT()));
        dbController.selectAllBooksFromDb();
    }

    public void borrowBook() {
        System.out.println("What title do you want to borrow?");
        String titleToBorrow = scanner.nextLine();

        dbController.selectBookAndProcess(titleToBorrow, "B");

    }

    public void returnBook() {
        System.out.println("What title do you want to return?");
        String titleToReturn = scanner.nextLine();

        dbController.selectBookAndProcess(titleToReturn, "R");

    }

    public void checkBookStatus() {
        System.out.println("What title do you want to check?");
        String titleToCheck = scanner.nextLine();

        dbController.selectBookAndProcess(titleToCheck, "C");

    }
}
