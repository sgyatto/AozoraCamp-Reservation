package com.example.aozoracampreservation.domain.model;

import lombok.Data;

import java.time.LocalDate;

/**
 * サイト空き状況
 */
@Data
public class SiteAvailability {
	private LocalDate calendarDate;
	private int siteTypeId;
	private int availabilityCount;
	private int maxCount;
}
