package me.alapaty.bankholidays.service;

import me.alapaty.bankholidays.beans.Event;
import me.alapaty.bankholidays.beans.Holiday;
import me.alapaty.bankholidays.beans.Weather;
import me.alapaty.bankholidays.exceptions.CityNotFoundException;

import java.util.Date;
import java.util.List;

public interface BankHolidayService {
    List<Event> retrieveBankHolidaysBetweenDates(Date startDate, Date endDate);

    String retrieveWOEIDForCity(String city) throws CityNotFoundException;

    @SuppressWarnings("unchecked")
    List<Weather> findWeatherForCityOnDate(String woeId, Date date);

    List<Holiday> retrieveTemperaturesForBankHolidayForCityBetweenDates(String city, String startDate, String endDate) throws CityNotFoundException;
}
