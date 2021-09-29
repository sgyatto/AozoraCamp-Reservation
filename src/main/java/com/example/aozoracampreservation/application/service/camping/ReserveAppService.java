package com.example.aozoracampreservation.application.service.camping;

import com.example.aozoracampreservation.domain.model.*;
import com.example.aozoracampreservation.domain.service.*;
import com.example.aozoracampreservation.exception.BusinessException;
import com.example.aozoracampreservation.exception.SystemException;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * キャンプ予約 Application Service
 */
@Service
public class ReserveAppService {

	private final MemberService memberService;
	private final SiteRateService siteRateService;
	private final SiteAvailabilityService siteAvailabilityService;
	private final ReservationService reservationService;
	private final ReservationDetailService reservationDetailService;
	private final MessageSource messageSource;

	public ReserveAppService(MemberService memberService, SiteRateService siteRateService,
							 SiteAvailabilityService siteAvailabilityService, ReservationService reservationService,
							 ReservationDetailService reservationDetailService, MessageSource messageSource) {
		this.memberService = memberService;
		this.siteRateService = siteRateService;
		this.siteAvailabilityService = siteAvailabilityService;
		this.reservationService = reservationService;
		this.reservationDetailService = reservationDetailService;
		this.messageSource = messageSource;
	}

	/**
	 * 予約情報組み立て
	 * @param stayInfo 宿泊情報
	 * @return 予約情報
	 */
	public Reservation buildReservation(StayInfo stayInfo, UserInfo userInfo) {
		Reservation reservation = new Reservation(
				stayInfo.getSiteTypeId(),
				stayInfo.getDateFrom(),
				stayInfo.getStayDays(),
				stayInfo.getNumberOfPeople(),
				null,
				null,
				1, // インターネット受付
				userInfo.getId(),
				userInfo.getName(),
				userInfo.getMail(),
				userInfo.getPhoneNumber()
				);
		// 予約詳細リストを生成
		reservation.setReservationDetails(this.makeReservationDetail(stayInfo));
		reservation.calcTotalAmountTaxInclAndSalesTax();
		return reservation;
	}

	/**
	 * 会員情報取得（ID指定）
	 * @param memberId  会員ID
	 * @return 会員情報
	 */
	public Member findMemberById(int memberId) {
		return memberService.findById(memberId)
				.orElseThrow(() -> new SystemException(messageSource.getMessage("exception.dataNotFound",
						new String[] {String.valueOf(memberId)}, Locale.JAPAN)));
	}

	/**
	 * 予約詳細リスト生成
	 * @param stayInfo	宿泊情報
	 * @return 予約詳細リスト
	 */
	private List<ReservationDetail> makeReservationDetail(StayInfo stayInfo) {
		List<LocalDate> dates = stayInfo.getDaysOfStay();
		return dates.stream().map(date -> {
			SiteRate siteRate = siteRateService.findSiteRate(stayInfo.getSiteTypeId(), date);
			return new ReservationDetail(date, siteRate);
		}).collect(Collectors.toList());
	}

	/**
	 * キャンプ予約<br>
	 * サイト空き状況の在庫を減らし、予約登録を行う。
	 * @param reservation	予約
	 */
	@Transactional(rollbackFor=Exception.class)
	public void saveReservation(Reservation reservation) {
		StayInfo stayInfo = new StayInfo();
		stayInfo.setSiteTypeId(reservation.getSiteTypeId());
		stayInfo.setDateFrom(reservation.getDateFrom());
		stayInfo.setStayDays(reservation.getStayDays());
		stayInfo.setNumberOfPeople(reservation.getNumberOfPeople());

		// サイト空き状況の在庫を減らし、サイトを確保
		this.reduceAvailabilityCount(stayInfo);

		// 予約登録
		reservationService.createReservation(reservation);
		// 予約詳細登録
		reservationDetailService.createReservationDetails(
				reservation.getReservationDetails(), reservation.getId());
	}

	/**
	 * サイト空き状況減算処理<br>
	 * サイト空き状況減算処理を行う。満室の日程がある場合はBusinessExceptionをthrowする。
	 * @param stayInfo 宿泊情報
	 */
	private void reduceAvailabilityCount(StayInfo stayInfo) {
		int updateCount = siteAvailabilityService.reduceAvailabilityCount(
					stayInfo.getSiteTypeId(), stayInfo.getDateFrom(), stayInfo.getDateTo());
		if (updateCount != stayInfo.getStayDays()) {
			throw new BusinessException(messageSource.getMessage("exception.siteIsNotAvailable", null, Locale.JAPAN));
		}
	}

}
