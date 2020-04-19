package me.alapaty.bankholidays.exceptions;

import lombok.Data;

@Data
public class CityNotFoundException extends Exception {
    String message;
    public CityNotFoundException(String message) {
        this.message = message;
    }
}
