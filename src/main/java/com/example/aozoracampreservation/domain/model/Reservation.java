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
			Integer siteTypeId, LocalDate dateFrom, Integer stayDays,
			Integer numberOfPeople, BigDecimal totalAmountTaxIncl, BigDecimal salesTax,
			Integer reservationMethod, Integer memberId, String nonMemberName, String nonMemberMail,
			String nonMemberPhoneNumber
	) {
		if (siteTypeId == null || dateFrom == null || stayDays == null ||
			numberOfPeople == null || totalAmountTaxIncl == null || salesTax == null ||
			reservationMethod == null) {
			throw new IllegalArgumentException("必須項目が設定されていません。");
		}

		if (!isValidUserInfo(memberId, nonMemberName, nonMemberMail, nonMemberPhoneNumber)) {
			throw new IllegalArgumentException("引数が不正です。");
		}

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
	 * ユーザー情報期限判定<br>
	 * 会員IDが設定済みかつ非会員情報が設定済みの場合false,
	 * 会員IDが未設定かつ非会員情報が設定済みの場合false,
	 * それ以外はtrueを返す。
	 * @param memberId 会員ID
	 * @param nonMemberName 名前（非会員）
	 * @param nonMemberMail	メールアドレス（非会員）
	 * @param nonMemberPhoneNumber 電話番号（非会員）
	 * @return 判定値
	 */
	private boolean isValidUserInfo(Integer memberId, String nonMemberName, String nonMemberMail, String nonMemberPhoneNumber) {
		return (memberId != null && nonMemberName == null && nonMemberMail == null && nonMemberPhoneNumber == null)
				|| (memberId == null && nonMemberName != null && nonMemberMail != null && nonMemberPhoneNumber != null);
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
