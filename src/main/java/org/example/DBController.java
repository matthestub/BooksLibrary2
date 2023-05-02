package org.example;

import com.diogonunes.jcolor.Ansi;
import com.diogonunes.jcolor.Attribute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBController {

    public static final String DB_NAME = "books";
    public static final String SELECT_ALL_QUERY = "SELECT * FROM " + DB_NAME;
    public static final String SELECT_BY_TITLE_QUERY = "SELECT * FROM " + DB_NAME + " WHERE b_title = ?";
    public static final String DELETE_QUERY = "DELETE FROM " + DB_NAME + " WHERE b_title = ?";
    public static final String INSERT_QUERY = "INSERT INTO " + DB_NAME + "(b_author, b_title, b_releaseYear, b_available) VALUES(?, ?, ?, ?)";
    public static final String UPDATE_QUERY = "UPDATE " + DB_NAME + " SET b_available = ? WHERE b_title = ?";


    private final Connection connection;

    DBController(Connection connection) {
        this.connection = connection;
    }

    public void addBookToDb(String author, String title, String releaseYear, boolean isAvailable) {


        if (!checkIfBookExists(title)) {
            try {
                PreparedStatement statement = connection.prepareStatement(INSERT_QUERY);
                statement.setString(1, author);
                statement.setString(2, title);
                statement.setInt(3, Integer.parseInt(releaseYear));
                statement.setBoolean(4, isAvailable);
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected == 0) {
                    System.out.println(Ansi.colorize("Cannot add this book to the DB.", Attribute.RED_TEXT()));
                } else {
                    System.out.println(Ansi.colorize("Book successfully added.", Attribute.GREEN_TEXT()));
                }
            } catch (SQLException e) {
                System.out.println("Exception while adding book to DB: " + e.getMessage());
            }
        } else {
            System.out.println(Ansi.colorize("This book already exists in DB. " +
                    "Try again with another title.", Attribute.RED_TEXT()));
        }
    }

    public void deleteBookFromDb(String title) {

        if (checkIfBookExists(title)) {

            try {
                PreparedStatement statement = connection.prepareStatement(DELETE_QUERY);
                statement.setString(1, title);
                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    System.out.println(Ansi.colorize("Cannot delete this book from DB.", Attribute.RED_TEXT()));
                } else {
                    System.out.println(Ansi.colorize("Book successfully deleted.", Attribute.GREEN_TEXT()));
                }

            } catch (SQLException e) {
                System.out.println("Exception while deleting book from DB: " + e.getMessage());
            }
        } else {
            System.out.println(Ansi.colorize("This book doesn't exists in DB.", Attribute.RED_TEXT()));
        }
    }

    public void selectAllBooksFromDb() {

        try {
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                printResultSet(resultSet);
            } else {
                System.out.println(Ansi.colorize("You have no books in your library.", Attribute.RED_TEXT()));
            }

        } catch (SQLException e) {
            System.out.println("Exception while selecting all books from DB: " + e.getMessage());
        }
    }

    public void selectBookAndProcess(String title, String operationType) {

        if (checkIfBookExists(title)) {

            try {
                PreparedStatement selectionStatement = connection.prepareStatement(SELECT_BY_TITLE_QUERY);
                selectionStatement.setString(1, title);
                ResultSet resultRow = selectionStatement.executeQuery();

                if (resultRow.next()) {

                    switch (operationType) {
                        case "B" -> borrowBookFromLibrary(resultRow, title);
                        case "R" -> returnBookToLibrary(resultRow, title);
                        case "C" -> printResultSet(resultRow);
                    }

                } else {
                    System.out.println(Ansi.colorize("This book is already borrowed.", Attribute.RED_TEXT()));
                }

            } catch (SQLException e) {
                System.out.println("Exception while selecting and processing title: " + e.getMessage());
            }
        } else {
            System.out.println(Ansi.colorize("This book doesn't exists in DB.", Attribute.RED_TEXT()));
        }
    }

    private void borrowBookFromLibrary(ResultSet resultRow, String titleToBorrow) throws SQLException {

        if (resultRow.getBoolean(5)) {

            PreparedStatement updateStatement = connection.prepareStatement(UPDATE_QUERY);
            updateStatement.setBoolean(1, false);
            updateStatement.setString(2, titleToBorrow);
            int rowsAffected = updateStatement.executeUpdate();

            if (rowsAffected == 1) {
                System.out.println(Ansi.colorize("Book successfully borrowed.", Attribute.GREEN_TEXT()));
            } else {
                System.out.println(Ansi.colorize("Something went wrong while borrowing the book.", Attribute.RED_TEXT()));
            }
        } else {
            System.out.println(Ansi.colorize("This book is already borrowed.", Attribute.RED_TEXT()));
        }
    }

    private void returnBookToLibrary(ResultSet resultRow, String titleToReturn) throws SQLException {

        if (!resultRow.getBoolean(5)) {

            PreparedStatement updateStatement = connection.prepareStatement(UPDATE_QUERY);
            updateStatement.setBoolean(1, true);
            updateStatement.setString(2, titleToReturn);
            int rowsAffected = updateStatement.executeUpdate();

            if (rowsAffected == 1) {
                System.out.println(Ansi.colorize("Book successfully returned.", Attribute.GREEN_TEXT()));
            } else {
                System.out.println(Ansi.colorize("Something went wrong while returning the book.", Attribute.RED_TEXT()));
            }
        } else {
            System.out.println(Ansi.colorize("This book is available so you can not return it.", Attribute.RED_TEXT()));
        }
    }


    public boolean checkIfBookExists(String title) {

        boolean isBookInDB = false;

        try {
            PreparedStatement statement = connection.prepareStatement(SELECT_BY_TITLE_QUERY);
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();

            isBookInDB = resultSet.next();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return isBookInDB;
    }

    public String checkIfBookIsAvailable(boolean isAvailable) {
        if (isAvailable) {
            return "YES";
        } else {
            return "NO";
        }
    }

    public void printResultSet(ResultSet resultSet) throws SQLException {
        do {
            System.out.println(Ansi.colorize("| " + resultSet.getString(1) + " | " + resultSet.getString(2) + " | " + resultSet.getString(3) + " | "
                            + resultSet.getString(4) + " | "
                            + checkIfBookIsAvailable(resultSet.getBoolean(5)) + " |",
                    Attribute.GREEN_TEXT()));
        } while (resultSet.next());
    }

}
