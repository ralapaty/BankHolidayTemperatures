package me.alapaty.bankholidays.beans;

import java.util.Date;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Holiday {
	@Getter @Setter
	private Date date;
	@Getter @Setter
	private String title;
	@Getter @Setter
	private Double low;
	@Getter @Setter
	private Double high;}
