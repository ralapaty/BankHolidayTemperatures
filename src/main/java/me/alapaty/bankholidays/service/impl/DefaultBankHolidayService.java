package me.alapaty.bankholidays.service.impl;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Headers;
import com.jayway.restassured.response.Response;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import me.alapaty.bankholidays.beans.*;
import me.alapaty.bankholidays.exceptions.CityNotFoundException;
import me.alapaty.bankholidays.util.BankHolidaysUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

import static me.alapaty.bankholidays.util.BankHolidaysUtils.formatDate;
import static me.alapaty.bankholidays.util.BankHolidaysUtils.parseDate;


@Service
@Slf4j
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class DefaultBankHolidayService implements me.alapaty.bankholidays.service.BankHolidayService {

    @Value("${uk.gov.bankholidays.api}")
    private String govUKBankHolidaysAPIURL;
    @Value("${com.metaweather.location.search.api}")
    private String woeidAPI;
    @Value("${com.metaweather.location.api}")
    private String locationAPI;
    private static final Header type = new com.jayway.restassured.response.Header("Content-Type", "application/json");
    private static final Header accept = new com.jayway.restassured.response.Header("Accept", "application/json");
    private static final Headers headers = new com.jayway.restassured.response.Headers(type, accept);

    @Getter
    private List<Event> bankHolidays;

    @PostConstruct
    private void init() throws UnsupportedEncodingException {
        log.info("Loading Bank Holiday data during service startup from gov.uk API URL: {}", this.govUKBankHolidaysAPIURL);
        Response response = RestAssured.given().relaxedHTTPSValidation().headers(headers).get(this.govUKBankHolidaysAPIURL);
        UKBankHoliday ukBankHoliday = response.getBody().as(UKBankHoliday.class);
        this.bankHolidays = new ArrayList<Event>();
        this.bankHolidays.addAll(ukBankHoliday.getEnglandAndWales().getEvents());
        this.bankHolidays.addAll(ukBankHoliday.getScotland().getEvents());
        this.bankHolidays.addAll(ukBankHoliday.getNorthernIreland().getEvents());
        log.info("Number of Bank Holidays Loaded from Gov.uk API are {}", this.bankHolidays.size());
    }

    @Override
    public List<Event> retrieveBankHolidaysBetweenDates(Date startDate, Date endDate) {
        List<Event> matchedDates = bankHolidays.stream()
                .filter(event -> event.getDate().equals(startDate)
                        || event.getDate().equals(endDate)
                        || event.getDate().after(startDate) && event.getDate().before(endDate))
                .collect(Collectors.toList());
        Collections.sort(matchedDates, (e1, e2) -> e1.getDate().compareTo(e2.getDate()));
        return matchedDates;
    }

    @Override
    public String retrieveWOEIDForCity(String city) throws CityNotFoundException {
        log.info("Retrieving WOEID for city: {} from API: {}", city, woeidAPI);
        Response response = RestAssured.given().relaxedHTTPSValidation().headers(headers).queryParam("query", city).get(woeidAPI);
        Location[] locations = response.getBody().as(Location[].class);
        if (locations.length > 0) {
            log.info("Retrieved WOEID: {} for city: {}", locations[0].getWoeid(), city);
            return locations[0].getWoeid();
        } else {
            String errorMessage = String.format("Invalid city %s", city);
            log.error(errorMessage);
            throw new CityNotFoundException(errorMessage);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Weather> findWeatherForCityOnDate(String woeId, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (StringUtils.isNotEmpty(woeId)) {
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            log.info("Retrieving Weather for WOEID: {} for date: {} from API URL: {}", woeId, formatDate(date), locationAPI);
            Response response = RestAssured.given().headers(headers).pathParam("woeid", woeId).pathParam("year", year).pathParam("month", month).pathParam("day", day).relaxedHTTPSValidation().get(locationAPI);
            Weather[] weather = response.getBody().as(Weather[].class);
            if (weather.length > 0) {
                return Arrays.asList(weather);
            } else {
                String errorMessage = String.format("Weather Data Not Found for WOEID: %s and Date: %s", woeId, formatDate(date));
                log.error(errorMessage);
            }
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<Holiday> retrieveTemperaturesForBankHolidayForCityBetweenDates(String city, String startDate, String endDate) throws CityNotFoundException {
        List<Holiday> holidays = new ArrayList<>();
        String woeidForCity = retrieveWOEIDForCity(city);
        if (StringUtils.isNotEmpty(woeidForCity)) {
            List<Event> matched;
            if (StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate)) {
                matched = retrieveBankHolidaysBetweenDates(parseDate(startDate), parseDate(endDate));
            } else {
                matched = bankHolidays;
            }
            log.info("No of Matched Bank Holidays {}", matched.size());
            for (Event event : matched) {
                List<Weather> weatherForCityDate = findWeatherForCityOnDate(woeidForCity, event.getDate());
                if (!CollectionUtils.isEmpty(weatherForCityDate)) {
                    Optional<Weather> min = weatherForCityDate.stream().min(Comparator.comparing(Weather::getMin_temp));
                    Optional<Weather> max = weatherForCityDate.stream().max(Comparator.comparing(Weather::getMax_temp));
                    holidays.add(new Holiday(formatDate(event.getDate()), event.getTitle(), min.get().getMin_temp(), max.get().getMax_temp()));
                }
            }
        }
        return holidays;
    }
}
