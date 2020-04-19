package me.alapaty.bankholidays.exceptions;

public class WeatherDataNotFound extends Exception {
    String message;
    public WeatherDataNotFound(String errorMessage) {
        this.message = errorMessage;
    }
}
