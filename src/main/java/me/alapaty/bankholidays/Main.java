package me.alapaty.bankholidays;


import java.util.List;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import me.alapaty.bankholidays.beans.ErrorResponse;
import me.alapaty.bankholidays.exceptions.CityNotFoundException;
import me.alapaty.bankholidays.exceptions.WeatherDataNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.alapaty.bankholidays.beans.BankHolidayService;
import me.alapaty.bankholidays.beans.Holiday;

import javax.ws.rs.core.Response;

@RestController
public class Main {
    private Logger log = LoggerFactory.getLogger(Main.class);
    @Autowired
    private BankHolidayService bankHolidayService;

    @GetMapping(path = "/{city}/temps")
    @ApiOperation(value = "Return min, max temperatures for a UK city on bank holidays", notes = "Return min, max temperatures for a UK city on bank holidays", response = Holiday[].class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Retrieved min, max temperatures for a UK city on bank holidays", response = Holiday[].class), @ApiResponse(code = 400, message = "Either invalid city entered or no data found for city, and date", response = ErrorResponse.class)})
    public ResponseEntity findBankHolidayTemperaturesByCity(@PathVariable String city, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate) {
        List<Holiday> holidays = null;
        ErrorResponse errorResponse = new ErrorResponse();
        try {
            holidays = bankHolidayService.retrieveTemperaturesForBankHolidayForCityBetweenDates(city, startDate, endDate);
        } catch (CityNotFoundException e) {
            errorResponse.setErrorMessage(e.getMessage());
            errorResponse.setStatusCode(Response.Status.BAD_REQUEST);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        log.info("Bank Holiday Temperatures for city {} are {}", city, holidays);
        return ResponseEntity.ok(holidays);
    }
}
