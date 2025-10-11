package com.playa.exception;

public class SongLimitExceededException extends RuntimeException {
    public SongLimitExceededException(String message) {
        super(message);
    }
}