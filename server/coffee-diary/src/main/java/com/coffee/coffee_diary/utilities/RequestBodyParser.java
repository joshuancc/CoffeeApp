package com.coffee.coffee_diary.utilities;

import java.util.Arrays;

public final class RequestBodyParser {
    private final byte[] requestBody;
    private int startIndex;

    public RequestBodyParser(byte[] requestBody) {
        this.requestBody = requestBody;
        this.startIndex = 0;
    }

    public String readLineString(int limit) {
        // Check whether the request body is already fully read
        if (this.startIndex >= this.requestBody.length) {
            return null;
        }

        // Search for a newline character from the current reading position
        int endIndex = Math.min(this.requestBody.length, this.startIndex + limit);
        for (int i = this.startIndex; i < endIndex; i++) {
            if (this.requestBody[i] == (byte) 10) {
                String result = new String(this.requestBody, this.startIndex, i - this.startIndex);
                this.startIndex = i + 1;
                return result;
            }
        }

        // If no newline character was found return the characters read as a string
        String result = new String(this.requestBody, this.startIndex, endIndex - this.startIndex);
        this.startIndex = endIndex + 1;
        return result;
    }

    public byte[] readLineBytes(int limit) {
        // Check whether the request body is already fully read
        if (this.startIndex >= this.requestBody.length) {
            return null;
        }

        // Search for a newline character from the current reading position
        int endIndex = Math.min(this.requestBody.length, this.startIndex + limit);
        for (int i = this.startIndex; i < endIndex; i++) {
            if (this.requestBody[i] == (byte) 10) {
                byte[] result = Arrays.copyOfRange(this.requestBody, this.startIndex, i);
                this.startIndex = i + 1;
                return result;
            }
        }

        // If no newline character was found return the characters read as an array of bytes
        byte[] result = Arrays.copyOfRange(this.requestBody, startIndex, endIndex);
        this.startIndex = endIndex + 1;
        return result;
    }
}
