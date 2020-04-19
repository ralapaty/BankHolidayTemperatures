package me.alapaty.bankholidays.exceptions;

import lombok.Data;

@Data
public class WeatherDataNotFound extends Exception {
    String message;
    public WeatherDataNotFound(String errorMessage) {
        this.message = errorMessage;
    }
}
