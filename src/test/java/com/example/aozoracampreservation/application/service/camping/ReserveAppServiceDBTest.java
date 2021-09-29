package com.example.aozoracampreservation.application.service.camping;

import com.example.aozoracampreservation.domain.model.*;
import com.example.aozoracampreservation.domain.service.ReservationService;
import com.example.aozoracampreservation.domain.service.SiteAvailabilityService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
class ReserveAppServiceDBTest {
	@Autowired ReserveAppService appService;
	@Autowired SiteAvailabilityService siteAvailabilityService;
	@Autowired ReservationService reservationService;

	@Nested
	class ConfirmInfo2DBTest {
		@Test
		void 予約情報を取得できる() {
			StayInfo stayInfo = new StayInfo();
			stayInfo.setSiteTypeId(1);
			stayInfo.setDateFrom(LocalDate.of(2021, 9, 30));
			stayInfo.setStayDays(2);
			stayInfo.setNumberOfPeople(3);
			stayInfo.setSiteTypeName("区画サイト（AC電源なし）");

			UserInfo userInfo = new UserInfo();
			userInfo.setName("テスト名前");
			userInfo.setMail("ex@test.com");
			userInfo.setPhoneNumber("09012345678");

			Reservation reservation = appService.buildReservation(stayInfo, userInfo);

			assertEquals(BigDecimal.valueOf(7700), reservation.getTotalAmountTaxIncl());
			assertEquals(BigDecimal.valueOf(700), reservation.getSalesTax());

			List<ReservationDetail> list = reservation.getReservationDetails();

			ReservationDetail each = list.get(0);
			assertEquals(LocalDate.of(2021, 9, 30), each.getReservationDate());
			assertEquals(BigDecimal.valueOf(4000), each.getSiteRate());
			assertEquals(BigDecimal.valueOf(0.10).setScale(2), each.getTaxRate());
			assertEquals("ハイシーズン料金", each.getRateTypeName());

			each = list.get(1);
			assertEquals(LocalDate.of(2021, 10, 1), each.getReservationDate());
			assertEquals(BigDecimal.valueOf(3000), each.getSiteRate());
			assertEquals(BigDecimal.valueOf(0.10).setScale(2), each.getTaxRate());
			assertEquals("通常料金", each.getRateTypeName());
		}
	}

	@Nested
	@Transactional
	class SaveReservation2DBTest {
		@Test
		void サイト空き状況の在庫を減らして予約と予約詳細を登録できる() {
			Integer siteTypeId = 1;
			LocalDate dateFrom = LocalDate.of(2021, 9, 30);
			Integer stayDays = 2;

			ReservationDetail detail1 = new ReservationDetail(
					null,	dateFrom, new BigDecimal(4000), BigDecimal.valueOf(0.10), "ハイシーズン料金");
			ReservationDetail detail2 = new ReservationDetail(
					null,	dateFrom.plusDays(1), new BigDecimal(3000), BigDecimal.valueOf(0.10), "通常料金");
			List<ReservationDetail> details = List.of(detail1, detail2);

			Reservation reservation = new Reservation(
					siteTypeId, dateFrom, stayDays, 2, new BigDecimal(7700), new BigDecimal(700),
					1, 1, null, null, null);
			reservation.setReservationDetails(details);


			// サイト空き状況の現在在庫
			LocalDate dateTo = dateFrom.plusDays(1);
			List<SiteAvailability> siteListBefore = siteAvailabilityService.findSiteAvailability(1, dateFrom, dateTo);

			// 予約
			appService.saveReservation(reservation);
			List<SiteAvailability> siteListAfter = siteAvailabilityService.findSiteAvailability(1, dateFrom, dateTo);

			assertEquals(siteListBefore.get(0).getAvailabilityCount() - 1, siteListAfter.get(0).getAvailabilityCount());
			assertEquals(siteListBefore.get(1).getAvailabilityCount() - 1, siteListAfter.get(1).getAvailabilityCount());
			Reservation reservationAfter = reservationService.findReservationDetailsById(1).get();
			assertEquals(2, reservationAfter.getReservationDetails().size());
			assertEquals(dateFrom, reservationAfter.getReservationDetails().get(0).getReservationDate());
			assertEquals(dateFrom.plusDays(1), reservationAfter.getReservationDetails().get(1).getReservationDate());
		}
	}
}