package dev.Innocent.EventManagementSystem.validation;


public class PasswordValidator {
        private static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$";
        public static boolean isValid(String password) {
            return password.matches(PASSWORD_PATTERN) && password.length() >= 8 && password.length() <= 20;
        }

}
