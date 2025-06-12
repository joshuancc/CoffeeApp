package com.coffee.coffee_diary.schemas.users;

import com.coffee.coffee_diary.utilities.RequestBodyParser;

public final class LoginUserInput {
    private final String email;
    private final byte[] password;

    private final String error;

    private static final int MAX_EMAIL_LENGTH = 254;
    private static final int MAX_PASSWORD_LENGTH = 71;

    public static LoginUserInput fromRequestBody(byte[] requestBody) {
        // Initialize request body parser
        RequestBodyParser requestBodyParser = new RequestBodyParser(requestBody);

        // Parse and validate email
        String email = requestBodyParser.readLineString(MAX_EMAIL_LENGTH + 1);
        if (email == null) {
            return new LoginUserInput(null, null, "Email required");
        }

        if (email.length() > MAX_EMAIL_LENGTH) {
            return new LoginUserInput(null, null, "Email cannot exceed 254 characters");
        }

        // Parse and validate password
        byte[] password = requestBodyParser.readLineBytes(MAX_PASSWORD_LENGTH + 1);
        if (password == null) {
            return new LoginUserInput(null, null, "Password required");
        }

        if (password.length > MAX_PASSWORD_LENGTH) {
            return new LoginUserInput(null, null, "Password cannot exceed 71 characters");
        }

        return new LoginUserInput(email, password, null);
    }

    public String getEmail() {
        return email;
    }

    public byte[] getPassword() {
        return password;
    }

    public String getError() {
        return error;
    }

    private LoginUserInput(String email, byte[] password, String error) {
        this.email = email;
        this.password = password;
        this.error = error;
    }
}
