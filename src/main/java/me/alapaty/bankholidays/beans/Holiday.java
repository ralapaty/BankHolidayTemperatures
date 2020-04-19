package me.alapaty.bankholidays.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Holiday {
    private String date;
    private String title;
    private Double low;
    private Double high;
}
