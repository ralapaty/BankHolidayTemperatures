package me.alapaty.bankholidays;


import java.util.List;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import me.alapaty.bankholidays.beans.ErrorResponse;
import me.alapaty.bankholidays.exceptions.CityNotFoundException;
import me.alapaty.bankholidays.exceptions.WeatherDataNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import me.alapaty.bankholidays.beans.BankHolidayService;
import me.alapaty.bankholidays.beans.Holiday;

@RestController
public class Main {
	private Logger log = LoggerFactory.getLogger(Main.class);
	@Autowired
	private BankHolidayService bankHolidayService;

	@GetMapping(path = "/{city}/temps")
	public Response findBankHolidayTemperaturesByCity(@PathVariable String city, @QueryParam(value = "startDate") String startDate, @QueryParam(value = "endDate") String endDate) {
		List<Holiday> holidays = null;
		ErrorResponse errorResponse = new ErrorResponse();
		try {
			holidays = bankHolidayService.retrieveTemperaturesForBankHolidayForCityBetweenDates(city, startDate, endDate);
		} catch (CityNotFoundException | WeatherDataNotFound e) {
			errorResponse.setErrorMessage(e.getMessage());
			errorResponse.setStatusCode(Response.Status.BAD_REQUEST);
			return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
		}
		log.info("Bank Holiday Temperatures for city {} are {}", city, holidays);
		return  Response.status(Response.Status.OK).entity(holidays).build();
	}
}
