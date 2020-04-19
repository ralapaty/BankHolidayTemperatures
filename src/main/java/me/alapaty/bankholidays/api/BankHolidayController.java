package me.alapaty.bankholidays.api;


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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Optional;

@RestController
@Validated
public class BankHolidayController {
    private Logger log = LoggerFactory.getLogger(BankHolidayController.class);
    @Autowired
    private BankHolidayService bankHolidayService;

    @GetMapping(path = "/{city}/temps")
    @ApiOperation(value = "Return min, max temperatures for a UK city on bank holidays", notes = "Return min, max temperatures for a UK city on bank holidays", response = Holiday[].class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Retrieved min, max temperatures for a UK city on bank holidays", response = Holiday[].class), @ApiResponse(code = 400, message = "Either invalid city entered or no data found for city, and date", response = ErrorResponse.class)})
    public ResponseEntity findBankHolidayTemperaturesByCity(@PathVariable("city") @NotEmpty(message = "City must not be empty and a valid real UK city/town/village Name") String city,
                                                            @RequestParam(name ="startDate", required = false) @Pattern(regexp = "^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$", message = "Date must be in format: yyyy-MM-dd eg: 2020-01-01") String startDate,
                                                            @RequestParam(name = "endDate", required = false )  @Pattern(regexp = "^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$", message = "Date must be in format: yyyy-MM-dd eg: 2020-12-31")  String endDate) throws CityNotFoundException {
        log.info("Bank Holiday Temperatures for city {}", city);
        return ResponseEntity.ok(bankHolidayService.retrieveTemperaturesForBankHolidayForCityBetweenDates(city, startDate, endDate));
    }
}
