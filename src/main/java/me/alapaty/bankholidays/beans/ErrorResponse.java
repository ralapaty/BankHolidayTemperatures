package me.alapaty.bankholidays.beans;

import lombok.Data;

import javax.ws.rs.core.Response;

@Data
public class ErrorResponse {
    private String errorMessage;
    private Response.Status statusCode;
}
