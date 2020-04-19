package me.alapaty.bankholidays.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UKBankHoliday {
    @JsonProperty("england-and-wales")
    private BankHoliday englandAndWales;
    @JsonProperty("scotland")
    private BankHoliday scotland;
    @JsonProperty("northern-ireland")
    private BankHoliday northernIreland;
}
