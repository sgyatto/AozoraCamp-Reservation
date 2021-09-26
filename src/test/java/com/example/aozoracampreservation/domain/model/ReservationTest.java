package com.example.aozoracampreservation.domain.model;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
class ReservationTest {
	final Integer siteTypeId = 3;
	final LocalDate dateFrom = LocalDate.of(2021, 9, 11);
	final Integer stayDays = 2;
	final Integer numberOfPeople = 4;
	final BigDecimal totalAmountTaxIncl = new BigDecimal(1100);
	final BigDecimal salesTax =  new BigDecimal(100);
	final Integer reservationMethod = 1;

	@Nested
	class ConstructorTest {
		@Test
		void Reservation新規作成_会員() {
			Integer memberId = 10;
			Reservation reservation = new Reservation(
					siteTypeId, dateFrom, stayDays, numberOfPeople, totalAmountTaxIncl,
					salesTax, reservationMethod, memberId, null, null, null
			);

			assertEquals(siteTypeId, reservation.getSiteTypeId());
			assertEquals(dateFrom, reservation.getDateFrom());
			assertEquals(stayDays, reservation.getStayDays());
			assertEquals(numberOfPeople, reservation.getNumberOfPeople());
			assertEquals(totalAmountTaxIncl, reservation.getTotalAmountTaxIncl());
			assertEquals(salesTax, reservation.getSalesTax());
			assertEquals(reservationMethod, reservation.getReservationMethod());
			assertEquals(memberId, reservation.getMemberId());
			assertNull(reservation.getNonMemberName());
			assertNull(reservation.getNonMemberMail());
			assertNull(reservation.getNonMemberPhoneNumber());
		}

		@Test
		void Reservation新規作成_非会員() {
			String nonMemberName = "テスト太郎" ;
			String nonMemberMail = "example@test.com";
			String nonMemberPhoneNumber = "09012345678";
			Reservation reservation = new Reservation(
					siteTypeId, dateFrom, stayDays, numberOfPeople, totalAmountTaxIncl,
					salesTax, reservationMethod, null, nonMemberName, nonMemberMail, nonMemberPhoneNumber
			);

			assertNull(reservation.getMemberId());
			assertEquals(nonMemberName,reservation.getNonMemberName());
			assertEquals(nonMemberMail,reservation.getNonMemberMail());
			assertEquals(nonMemberPhoneNumber,reservation.getNonMemberPhoneNumber());
		}

		@Test
		void 会員なのに非会員情報が設定済みだと例外() {
			Integer memberId = 10;
			String str = "テスト太郎" ;

			// nonMemberName が設定済み
			assertThrows(IllegalArgumentException.class, () -> {
				new Reservation(
						siteTypeId, dateFrom, stayDays, numberOfPeople, totalAmountTaxIncl,
						salesTax, reservationMethod, memberId, str, null, null
				);
			});
			// nonMemberMail が設定済み
			assertThrows(IllegalArgumentException.class, () -> {
				new Reservation(
						siteTypeId, dateFrom, stayDays, numberOfPeople, totalAmountTaxIncl,
						salesTax, reservationMethod, memberId, null, str, null
				);
			});
			// nonMemberPhoneNumber が設定済み
			assertThrows(IllegalArgumentException.class, () -> {
				new Reservation(
						siteTypeId, dateFrom, stayDays, numberOfPeople, totalAmountTaxIncl,
						salesTax, reservationMethod, memberId, null, null, str
				);
			});
		}

		@Test
		void 非会員なのに非会員情報が未設定だと例外() {
			String str = "テスト" ;

			// nonMemberName が未設定
			assertThrows(IllegalArgumentException.class, () -> {
				new Reservation(
						siteTypeId, dateFrom, stayDays, numberOfPeople, totalAmountTaxIncl,
						salesTax, reservationMethod, null, null, str, str
				);
			});
			// nonMemberMail が未設定
			assertThrows(IllegalArgumentException.class, () -> {
				new Reservation(
						siteTypeId, dateFrom, stayDays, numberOfPeople, totalAmountTaxIncl,
						salesTax, reservationMethod, null, str, null, str
				);
			});
			// nonMemberPhoneNumber が未設定
			assertThrows(IllegalArgumentException.class, () -> {
				new Reservation(
						siteTypeId, dateFrom, stayDays, numberOfPeople, totalAmountTaxIncl,
						salesTax, reservationMethod, null, str, str, null
				);
			});
		}

		@Test
		void 必須項目が未設定だと例外() {
			Integer memberId = 10;

			// siteTypeId が未設定
			assertThrows(IllegalArgumentException.class, () -> {
				new Reservation(
						null, dateFrom, stayDays, numberOfPeople, totalAmountTaxIncl,
						salesTax, reservationMethod, memberId, null, null, null
				);
			});
			// dateFrom が未設定
			assertThrows(IllegalArgumentException.class, () -> {
				new Reservation(
						siteTypeId, null, stayDays, numberOfPeople, totalAmountTaxIncl,
						salesTax, reservationMethod, memberId, null, null, null
				);
			});
			// stayDays が未設定
			assertThrows(IllegalArgumentException.class, () -> {
				new Reservation(
						siteTypeId, dateFrom, null, numberOfPeople, totalAmountTaxIncl,
						salesTax, reservationMethod, memberId, null, null, null
				);
			});
			// numberOfPeople が未設定
			assertThrows(IllegalArgumentException.class, () -> {
				new Reservation(
						siteTypeId, dateFrom, stayDays, null, totalAmountTaxIncl,
						salesTax, reservationMethod, memberId, null, null, null
				);
			});
			// totalAmountTaxIncl が未設定
			assertThrows(IllegalArgumentException.class, () -> {
				new Reservation(
						siteTypeId, dateFrom, stayDays, numberOfPeople, null,
						salesTax, reservationMethod, memberId, null, null, null
				);
			});
			// salesTax が未設定
			assertThrows(IllegalArgumentException.class, () -> {
				new Reservation(
						siteTypeId, dateFrom, stayDays, numberOfPeople, totalAmountTaxIncl,
						null, reservationMethod, memberId, null, null, null
				);
			});
			// reservationMethod が未設定
			assertThrows(IllegalArgumentException.class, () -> {
				new Reservation(
						siteTypeId, dateFrom, stayDays, numberOfPeople, totalAmountTaxIncl,
						salesTax, null, memberId, null, null, null
				);
			});
		}
	}

	@Nested
	class CanCancelTest {
		@Test
		void チェックインの3日前までならtrue() {
			// 基準日（dateFromの3日前）
			LocalDate today = LocalDate.of(2021, 9, 8);
			Reservation reservation = new Reservation(
					siteTypeId, dateFrom, stayDays, numberOfPeople, totalAmountTaxIncl,
					salesTax, reservationMethod, 10, null, null, null
			);

			try (var mock = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
				mock.when(LocalDate::now).thenReturn(today);
				assertTrue(reservation.canCancel());
			}
		}

		@Test
		void チェックインの2日前はfalse() {
			// 基準日（dateFromの4日前）
			LocalDate today = LocalDate.of(2021, 9, 9);
			Reservation reservation = new Reservation(
					siteTypeId, dateFrom, stayDays, numberOfPeople, totalAmountTaxIncl,
					salesTax, reservationMethod, 10, null, null, null
			);

			try (var mock = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
				mock.when(LocalDate::now).thenReturn(today);
				assertFalse(reservation.canCancel());
			}
		}
	}

	@Nested
	class IsCanceledTest {
		@Test
		void canceledAtが設定済みならtrue() {
			Reservation reservation = new Reservation(
					siteTypeId, dateFrom, stayDays, numberOfPeople, totalAmountTaxIncl,
					salesTax, reservationMethod, 10, null, null, null
			);
			reservation.setCanceledAt(LocalDateTime.of(2021, 9, 9, 1, 0));

			assertTrue(reservation.isCanceled());
		}

		@Test
		void canceledAtが未設定ならfalse() {
			Reservation reservation = new Reservation(
					siteTypeId, dateFrom, stayDays, numberOfPeople, totalAmountTaxIncl,
					salesTax, reservationMethod, 10, null, null, null
			);

			assertFalse(reservation.isCanceled());
		}
	}
}