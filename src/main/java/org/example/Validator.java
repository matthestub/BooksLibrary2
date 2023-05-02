package org.example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    public static final String AUTHOR_STRING_RULE = "^([a-zA-Z]+\\.?\\s?)+[a-zA-Z-]+$";

    public static final String TITLE_STRING_RULE = "^[A-Z][a-zA-Z1-9 ]+$";

    public static final String YEAR_STRING_RULE = "[1-2][0-9]{3}";

    public boolean validateAuthor(String author) {
        Pattern pattern = Pattern.compile(AUTHOR_STRING_RULE);
        Matcher matcher = pattern.matcher(author);

        return matcher.find();
    }

    public boolean validateTitle(String title) {
        Pattern pattern = Pattern.compile(TITLE_STRING_RULE);
        Matcher matcher = pattern.matcher(title);

        return matcher.find();
    }

    public boolean validateReleaseYear(String releaseYear) {
        Pattern pattern = Pattern.compile(YEAR_STRING_RULE);
        Matcher matcher = pattern.matcher(releaseYear);

        return matcher.find();
    }

}
