package me.alapaty.bankholidays.beans;

import java.util.Date;

import lombok.Data;

@Data
public class Event {
	public static final String TITLE = "title";
	public static final String DATE = "date";

	private String title;
	private Date date;
	private String notes;
	private boolean bunting;
}
