package me.alapaty.bankholidays;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import me.alapaty.bankholidays.beans.ErrorResponse;
import me.alapaty.bankholidays.beans.Holiday;
import me.alapaty.bankholidays.exceptions.CityNotFoundException;
import me.alapaty.bankholidays.service.BankHolidayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

@RestController
@Validated
public class Main {
    private Logger log = LoggerFactory.getLogger(Main.class);
    @Autowired
    private BankHolidayService bankHolidayService;

    @GetMapping(path = "/{city}/temps")
    @ApiOperation(value = "Return min, max temperatures for a UK city on bank holidays", notes = "Return min, max temperatures for a UK city on bank holidays", response = Holiday[].class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Retrieved min, max temperatures for a UK city on bank holidays", response = Holiday[].class), @ApiResponse(code = 400, message = "Either invalid city entered or no data found for city, and date", response = ErrorResponse.class)})
    public ResponseEntity findBankHolidayTemperaturesByCity(@PathVariable("city") @NotEmpty(message = "City must not be empty and a valid real UK city/town/village Name") String city,
                                                            @RequestParam("startDate") @NotEmpty @Pattern(regexp = "^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$", message = "Date must not be empty and in format: yyyy-MM-dd eg: 2020-01-01") String startDate,
                                                            @RequestParam("endDate") @NotEmpty @Pattern(regexp = "^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$", message = "Date must not be empty and in format: yyyy-MM-dd eg: 2020-12-31")  String endDate) {
        List<Holiday> holidays = null;
        ErrorResponse errorResponse = new ErrorResponse();
        try {
            holidays = bankHolidayService.retrieveTemperaturesForBankHolidayForCityBetweenDates(city, startDate, endDate);
        } catch (CityNotFoundException e) {
            errorResponse.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
        log.info("Bank Holiday Temperatures for city {} are {}", city, holidays);
        return ResponseEntity.ok(holidays);
    }
}
