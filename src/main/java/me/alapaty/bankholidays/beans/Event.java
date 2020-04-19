package me.alapaty.bankholidays.beans;

import java.util.Date;

import lombok.Data;

@Data
public class Event {
    private String title;
    private Date date;
    private String notes;
    private boolean bunting;
}
