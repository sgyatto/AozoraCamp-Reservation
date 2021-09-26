package com.example.aozoracampreservation.domain.model;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@SuppressWarnings("NonAsciiCharacters")
class StayInfoTest {

	@Nested
	class IsValidPeriodTest {
		@Test
		void 予約受付期間外の日程が含まれないならtrue() {
			// 基準日
			LocalDate today = LocalDate.of(2021, 9, 10);

			// 翌日
			StayInfo stayInfo1 = new StayInfo();
			stayInfo1.setDateFrom(LocalDate.of(2021, 9, 11));
			stayInfo1.setStayDays(5);

			// 翌日を含めて90日目
			StayInfo stayInfo2 = new StayInfo();
			stayInfo2.setDateFrom(LocalDate.of(2021, 12, 9));
			stayInfo2.setStayDays(1);

			try (MockedStatic<LocalDate> mock = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
				mock.when(LocalDate::now).thenReturn(today);
				assertTrue(stayInfo1.isValidPeriod());
				assertTrue(stayInfo2.isValidPeriod());
				mock.verify(LocalDate::now, times(3));
			}
		}

		@Test
		void 予約受付期間外の日程が含まるならfalse() {
			// 基準日
			LocalDate today = LocalDate.of(2021, 9, 10);

			// 基準日を含む
			StayInfo stayInfo1 = new StayInfo();
			stayInfo1.setDateFrom(LocalDate.of(2021, 9, 10));
			stayInfo1.setStayDays(5);

			// 翌日を含めて91日目を含む
			StayInfo stayInfo2 = new StayInfo();
			stayInfo2.setDateFrom(LocalDate.of(2021, 12, 9));
			stayInfo2.setStayDays(2);

			try (MockedStatic<LocalDate> mock = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
				mock.when(LocalDate::now).thenReturn(today);
				assertFalse(stayInfo1.isValidPeriod());
				assertFalse(stayInfo2.isValidPeriod());
				mock.verify(LocalDate::now, times(3));
			}
		}
	}

	@Nested
	class IsValidNumberOfPeopleTest {
		@Test
		void 上限人数を上回らない場合はtrue() {
			StayInfo stayInfo = new StayInfo();
			stayInfo.setNumberOfPeople(4);

			assertTrue(stayInfo.isValidNumberOfPeople(4));
			assertTrue(stayInfo.isValidNumberOfPeople(5));
		}

		@Test
		void 上限人数を上回る場合はfalse() {
			StayInfo stayInfo = new StayInfo();
			stayInfo.setNumberOfPeople(5);

			assertFalse(stayInfo.isValidNumberOfPeople(4));
		}
	}

	@Nested
	class GetDaysOfStayTest {
		@Test
		void 宿泊日リストが取得できる() {
			StayInfo stayInfo = new StayInfo();
			stayInfo.setDateFrom(LocalDate.of(2021, 9, 10));
			stayInfo.setStayDays(3);

			List<LocalDate> expect = List.of(
					LocalDate.of(2021, 9, 10)
					, LocalDate.of(2021, 9, 11)
					, LocalDate.of(2021, 9, 12)
			);

			assertEquals(expect, stayInfo.getDaysOfStay());
		}
	}

	@Nested
	class GetDateToTest {
		@Test
		void 最終宿泊日取得が取得できる() {
			StayInfo stayInfo = new StayInfo();
			stayInfo.setDateFrom(LocalDate.of(2021, 9, 10));
			stayInfo.setStayDays(3);
			LocalDate expect = LocalDate.of(2021, 9, 12);

			assertEquals(expect, stayInfo.getDateTo());
		}
	}
}