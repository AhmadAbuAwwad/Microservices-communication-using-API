package com.example.JWT.exception;
public class RefreshTokenException extends RuntimeException {
    public RefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public RefreshTokenException(String message) {
        super(message);
    }

    public RefreshTokenException(Throwable cause) {
        super(cause);
    }
}
