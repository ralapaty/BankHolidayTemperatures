package me.alapaty.bankholidays.beans;

import lombok.Data;

import java.util.List;

@Data
public class BankHoliday {
    private String division;
    private List<Event> events;
}
