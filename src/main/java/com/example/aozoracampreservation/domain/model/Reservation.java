package com.example.aozoracampreservation.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 予約
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Reservation {

	private static final int CANCEL_DEADLINE_NUMBER = 3;

	private Integer id;
	private Integer siteTypeId;
	private LocalDate dateFrom;
	private Integer stayDays;
	private Integer numberOfPeople;
	private BigDecimal totalAmountTaxIncl;
	private BigDecimal salesTax;
	private Integer reservationMethod;
	private Integer memberId;
	private String nonMemberName;
	private String nonMemberMail;
	private String nonMemberPhoneNumber;
	private LocalDateTime canceledAt;
	private LocalDateTime createdAt;
	private SiteType siteType;
	private Member member;
	private List<ReservationDetail> reservationDetails;

	public Reservation(
			int siteTypeId, LocalDate dateFrom, int stayDays,
			int numberOfPeople, BigDecimal totalAmountTaxIncl, BigDecimal salesTax,
			int reservationMethod, Integer memberId, String nonMemberName, String nonMemberMail,
			String nonMemberPhoneNumber
	) {
		this.siteTypeId = siteTypeId;
		this.dateFrom = dateFrom;
		this.stayDays = stayDays;
		this.numberOfPeople = numberOfPeople;
		this.totalAmountTaxIncl = totalAmountTaxIncl;
		this.salesTax = salesTax;
		this.reservationMethod = reservationMethod;
		this.memberId = memberId;
		this.nonMemberName = nonMemberName;
		this.nonMemberMail = nonMemberMail;
		this.nonMemberPhoneNumber = nonMemberPhoneNumber;
	}

	/**
	 * キャンセル期限判定<br>
	 * キャンセル期限（チェックイン日の3日前まで）を過ぎている場合はfalse, それ以外はtrueを返す。
	 * @return 判定値
	 */
	public boolean canCancel() {
		return dateFrom.isAfter(LocalDate.now().plusDays(CANCEL_DEADLINE_NUMBER - 1));
	}

	/**
	 * キャンセル済み判定<br>
	 * キャンセル済みの場合true, それ以外はfalseを返す。
	 * @return 判定値
	 */
	public boolean isCanceled() {
		return canceledAt != null;
	}

}
