package me.alapaty.bankholidays.beans;

import lombok.Data;

@Data
public class Location {
    public String title;
    public String location_type;
    public String woeid;
    public String latt_long;
}
