package com.example.aozoracampreservation.domain.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * サイト料金表
 */
@Data
public class SiteRatePivot {
	private Integer siteTypeId;
	private String siteTypeName;
	private BigDecimal rateBasic;
	private BigDecimal rateHighSeason;
}
