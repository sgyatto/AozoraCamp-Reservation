package com.example.aozoracampreservation.domain.model;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("NonAsciiCharacters")
class ReservationDetailTest {
	final private LocalDate reservationDate = LocalDate.of(2021, 9, 11);
	final private BigDecimal siteRate = new BigDecimal(1100);
	final private BigDecimal taxRate = new BigDecimal(100);
	final private String rateTypeName = "通常料金";

	@Nested
	class ConstructorTest {
		@Test
		void ReservationDetail新規作成() {
			var mockRateType = mock(RateType.class);
			doReturn(rateTypeName).when(mockRateType).getName();
			var mock = mock(SiteRate.class);
			doReturn(siteRate).when(mock).getRate();
			doReturn(taxRate).when(mock).getTaxRate();
			doReturn(mockRateType).when(mock).getRateType();

			ReservationDetail detail = new ReservationDetail(reservationDate, mock);

			assertEquals(reservationDate, detail.getReservationDate());
			assertEquals(siteRate, detail.getSiteRate());
			assertEquals(taxRate, detail.getTaxRate());
			assertEquals(rateTypeName, detail.getRateTypeName());
		}

		@Test
		void 必須項目が未設定だと例外() {
			var mockRateType = mock(RateType.class);
			doReturn(rateTypeName).when(mockRateType).getName();
			var mock = mock(SiteRate.class);
			doReturn(siteRate).when(mock).getRate();
			doReturn(taxRate).when(mock).getTaxRate();
			doReturn(mockRateType).when(mock).getRateType();

			// reservationDate が未設定
			assertThrows(IllegalArgumentException.class, () -> {
				new ReservationDetail(null, mock);
			});
			// siteRate が未設定
			assertThrows(IllegalArgumentException.class, () -> {
				new ReservationDetail(reservationDate, null);
			});
		}
	}
}