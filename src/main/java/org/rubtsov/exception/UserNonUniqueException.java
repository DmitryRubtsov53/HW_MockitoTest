package org.rubtsov.exception;

public class UserNonUniqueException extends RuntimeException {
    public UserNonUniqueException(String message) {
        super(message);
    }
}
