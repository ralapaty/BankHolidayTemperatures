package me.alapaty.bankholidays.exceptions;

public class CityNotFoundException extends Exception {
    String message;
    public CityNotFoundException(String message) {
        this.message = message;
    }
}
