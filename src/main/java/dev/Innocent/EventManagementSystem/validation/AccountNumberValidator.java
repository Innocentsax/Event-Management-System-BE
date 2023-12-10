package dev.Innocent.EventManagementSystem.validation;





import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountNumberValidator {

    private static final String ACCOUNT_NUMBER_REGEX = "^[0-9]{10}$";

    public static boolean isValid(String accountNumber) {

        Pattern pattern = Pattern.compile(ACCOUNT_NUMBER_REGEX);

        Matcher matcher = pattern.matcher(accountNumber);

        return matcher.matches();
    }

}



