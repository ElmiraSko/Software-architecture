package ru.erasko.rest;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}