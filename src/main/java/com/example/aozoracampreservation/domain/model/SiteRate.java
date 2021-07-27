package com.example.aozoracampreservation.domain.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * サイト料金
 */
@Data
public class SiteRate {

	private int siteTypeId;
	private int rateTypeId;
	private LocalDate dateFrom;
	private SiteType siteType;
	private RateType rateType;
	private BigDecimal rate;
	private BigDecimal taxRate;

}
