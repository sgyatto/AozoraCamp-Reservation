package com.example.aozoracampreservation.domain.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 宿泊情報
 */
@Data
public class StayInfo {

	private int siteTypeId;
	private LocalDate dateFrom;
	private int stayDays;
	private int numberOfPeople;
	private String siteTypeName;

	/**
	 * 予約受付期間判定<br>
	 * 予約受付期間は翌日〜90日間。
	 * 予約受付期間外の日程が含まれている場合はfalse, それ以外はtrueを返す。
	 * @return 判定値
	 */
	public boolean isValidPeriod() {

		// 翌日
		LocalDate tomorrow = LocalDate.now().plusDays(1);
		// 90日後
		LocalDate day90Later = LocalDate.now().plusDays(90);
		// 最終宿泊日
		LocalDate dateTo = this.getDateTo();

		if (dateFrom.isBefore(tomorrow) || dateTo.isAfter(day90Later)) {
			return false;
		}
		return true;
	}

	/**
	 * 人数上限判定<br>
	 * 引数で渡されるサイトの上限人数を上回る場合はfalse, それ以外はtrueを返す。
	 * @param upperLimit サイトの上限人数
	 * @return 判定値
	 */
	public boolean isValidNumberOfPeople(int upperLimit) {
		return numberOfPeople <= upperLimit;
	}

	/**
	 * 宿泊日リスト取得
	 * @return 宿泊日リスト
	 */
	public List<LocalDate> getDaysOfStay() {
		return dateFrom.datesUntil(dateFrom.plusDays(stayDays)).collect(Collectors.toList());
	}

	/**
	 * 最終宿泊日取得
	 * @return 最終宿泊日
	 */
	public LocalDate getDateTo() {
		return dateFrom.plusDays(stayDays - 1);
	}
}
