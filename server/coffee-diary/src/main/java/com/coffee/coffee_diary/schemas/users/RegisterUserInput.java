package com.coffee.coffee_diary.schemas.users;

import com.coffee.coffee_diary.utilities.RequestBodyParser;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;

public final class RegisterUserInput {
    private final String email;
    private final byte[] password;

    private final String error;

    private static final int MAX_EMAIL_LENGTH = 254;
    private static final int MAX_PASSWORD_LENGTH = 71;

    public static RegisterUserInput fromRequestBody(byte[] requestBody) {
        // Get request body parser instance
        RequestBodyParser requestBodyParser = new RequestBodyParser(requestBody);

        // Parse and validate email
        String email = requestBodyParser.readLineString(MAX_EMAIL_LENGTH + 1);
        if (email == null) {
            return new RegisterUserInput(null, null, "Email required");
        }

        if (email.length() > MAX_EMAIL_LENGTH) {
            return new RegisterUserInput(null, null, "Email cannot exceed 254 characters");
        }

        EmailValidator emailValidator = new EmailValidator();
        if (email.isEmpty() || !emailValidator.isValid(email, null)) {
            return new RegisterUserInput(null, null, "Invalid email");
        }

        // Parse and validate password
        byte[] password = requestBodyParser.readLineBytes(MAX_PASSWORD_LENGTH + 1);
        if (password == null) {
            return new RegisterUserInput(null, null, "Password required");
        }

        if (password.length > MAX_PASSWORD_LENGTH) {
            return new RegisterUserInput(null, null, "Password cannot exceed 71 characters");
        }

        if (password.length < 8) {
            return new RegisterUserInput(null, null, "Password must be at least 8 characters");
        }

        return new RegisterUserInput(email, password, null);
    }

    public String getEmail() {
        return this.email;
    }

    public byte[] getPassword() {
        return this.password;
    }

    public String getError() {
        return this.error;
    }

    private RegisterUserInput(String email, byte[] password, String error) {
        this.email = email;
        this.password = password;
        this.error = error;
    }
}
