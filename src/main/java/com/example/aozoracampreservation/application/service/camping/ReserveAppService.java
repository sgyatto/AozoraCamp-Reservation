package com.example.aozoracampreservation.application.service.camping;

import com.example.aozoracampreservation.domain.model.*;
import com.example.aozoracampreservation.domain.service.*;
import com.example.aozoracampreservation.exception.BusinessException;
import com.example.aozoracampreservation.exception.SystemException;
import com.example.aozoracampreservation.presentation.camping.ReserveConfirmDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
							 SiteAvailabilityService siteAvailabilityService, ReservationService reservationService, ReservationDetailService reservationDetailService, MessageSource messageSource) {
		this.memberService = memberService;
		this.siteRateService = siteRateService;
		this.siteAvailabilityService = siteAvailabilityService;
		this.reservationService = reservationService;
		this.reservationDetailService = reservationDetailService;
		this.messageSource = messageSource;
	}

	/**
	 * 予約内容確認情報取得
	 * @param stayInfo 宿泊情報
	 * @return 予約内容確認情報
	 */
	public ReserveConfirmDto confirmInfo(StayInfo stayInfo) {

		ReserveConfirmDto dto = new ReserveConfirmDto();

		// 予約詳細リストを生成
		List<ReservationDetail> reservationDetails = this.makeReservationDetail(stayInfo);
		// 宿泊料金の合計額算出
		ResultCalcTotal resultCalcTotal = calcTotalAmountTaxInclAndSalesTax(reservationDetails);

		// 予約内容確認情報に設定
		dto.setTotalAmountTaxIncl(resultCalcTotal.getTotalAmountTaxIncl());
		dto.setSalesTax(resultCalcTotal.getSalesTax());
		dto.setReservationDetails(reservationDetails);

		return dto;
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
	 * 宿泊料金の合計額算出<br>
	 * 予約詳細リストをもとに宿泊料金の合計額を算出する。
	 * 消費税率は宿泊日をもとに決定されるため、消費税率ごとに1度だけ積算したのち、合算する。
	 * @param details 予約詳細リスト
	 * @return 宿泊料金の合計額
	 */
	private ResultCalcTotal calcTotalAmountTaxInclAndSalesTax(List<ReservationDetail> details) {

		// 消費税率毎に料金をグループ化して集計（消費税率改定またぎ対応）
		// ex. {{0.08, 3000}, {0.10, 9000}}
		Map<BigDecimal, BigDecimal> map = details.stream().collect(
				Collectors.groupingBy(ReservationDetail::getTaxRate,
						Collectors.reducing(BigDecimal.ZERO, ReservationDetail::getSiteRate, BigDecimal::add)));

		// 合計金額（税抜）
		BigDecimal totalAmount = BigDecimal.ZERO;
		// 合計金額（税込）端数未処理
		BigDecimal totalAmountTaxInclBeforeRounding = BigDecimal.ZERO;

		for (Map.Entry<BigDecimal, BigDecimal> entry : map.entrySet()) {
			// key:消費税率
			BigDecimal taxRate = entry.getKey();
			// value: 消費税率毎の集計金額
			BigDecimal totalByTaxRate = entry.getValue();

			// 合計金額（税抜）
			totalAmount = totalAmount.add(totalByTaxRate);

			// 合計金額（税抜）端数未処理
			totalAmountTaxInclBeforeRounding = totalAmountTaxInclBeforeRounding.add(
					BigDecimal.ONE.add(taxRate).multiply(totalByTaxRate));
		}

		// 合計金額（税込）端数処理完了
		BigDecimal totalAmountTaxIncl = totalAmountTaxInclBeforeRounding.setScale(0, RoundingMode.FLOOR);

		return new ResultCalcTotal(totalAmountTaxIncl, totalAmountTaxIncl.subtract(totalAmount));
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
	 * キャンプ予約
	 * @param stayInfo	宿泊情報
	 * @param reserveConfirmDto	予約内容確認情報
	 * @param userInfo ユーザ―情報
	 */
	@Transactional(rollbackFor=Exception.class)
	public void saveReservation(StayInfo stayInfo, ReserveConfirmDto reserveConfirmDto, UserInfo userInfo) {

		// サイト空き状況の在庫を減らし、サイトを確保
		this.reduceAvailabilityCount(stayInfo);

		// 登録用予約
		Reservation reservation = new Reservation(
				stayInfo.getSiteTypeId(),
				stayInfo.getDateFrom(),
				stayInfo.getStayDays(),
				stayInfo.getNumberOfPeople(),
				reserveConfirmDto.getTotalAmountTaxIncl(),
				reserveConfirmDto.getSalesTax(),
				1, // インターネット受付
				userInfo.getId(),
				userInfo.getName(),
				userInfo.getMail(),
				userInfo.getPhoneNumber()
		);

		// 予約登録
		reservationService.createReservation(reservation);
		// 予約詳細登録
		reservationDetailService.createReservationDetails(
				reserveConfirmDto.getReservationDetails(), reservation.getId());
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

	/**
	 * 宿泊料金の合計額
	 */
	@AllArgsConstructor
	@Data
	static class ResultCalcTotal {
		private BigDecimal totalAmountTaxIncl;
		private BigDecimal salesTax;
	}


}
