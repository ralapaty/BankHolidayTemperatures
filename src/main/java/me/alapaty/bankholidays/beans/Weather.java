package me.alapaty.bankholidays.beans;

import lombok.Data;

import java.util.Date;

@Data
public class Weather {
    private String id;
    private Double air_pressure;
    private Date created;
    private Date applicable_date;
    private Double humidity;
    private Double min_temp;
    private Double max_temp;
    private Double predictability;
    private Double the_temp;
    private Double visibility;
    private String weather_state_abbr;
    private String weather_state_name;
    private Double wind_direction;
    private String wind_direction_compass;
    private Double wind_speed;
}
