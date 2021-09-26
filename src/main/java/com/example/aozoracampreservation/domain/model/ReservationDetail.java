package com.example.aozoracampreservation.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 予約詳細
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReservationDetail {

	private Integer reservationId;
	private LocalDate reservationDate;
	private BigDecimal siteRate;
	private BigDecimal taxRate;
	private String rateTypeName;

	public ReservationDetail(LocalDate reservationDate, SiteRate siteRate) {
		if (reservationDate == null || siteRate == null) {
			throw new IllegalArgumentException("必須項目が設定されていません。");
		}
		this.reservationDate = reservationDate;
		this.siteRate = siteRate.getRate();
		this.taxRate = siteRate.getTaxRate();
		this.rateTypeName = siteRate.getRateType().getName();;
	}

}
