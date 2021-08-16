package com.example.aozoracampreservation.application.service.api;

import com.example.aozoracampreservation.domain.model.SiteAvailability;
import com.example.aozoracampreservation.domain.service.SiteAvailabilityService;
import com.example.aozoracampreservation.exception.BadRequestException;
import com.example.aozoracampreservation.exception.SystemException;
import com.example.aozoracampreservation.presentation.api.ResultSiteAvailability;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

/**
 * サイト空き状況（スケジュール用）の検索 Application Service
 */
@Service
public class SiteAvailabilityRestAppService {

	private final SiteAvailabilityService siteAvailabilityService;
	private final MessageSource messageSource;
	public SiteAvailabilityRestAppService(SiteAvailabilityService siteAvailabilityService, MessageSource messageSource) {
		this.siteAvailabilityService = siteAvailabilityService;
		this.messageSource = messageSource;
	}

	/**
	 * サイト空き状況（スケジュール用）の検索
	 * @param siteTypeId サイトタイプID
	 * @param startDate 取得開始日
	 * @param endDate 取得終了日
	 * @return サイト空き状況リスト（スケジュール用）
	 */
	public List<ResultSiteAvailability> fetchSiteAvailabilityForSchedule(
			int siteTypeId, LocalDate startDate, LocalDate endDate) {

		// 最大取得範囲は縦6マス×横7マス
		long period = ChronoUnit.DAYS.between(startDate, endDate) + 1;
		if (period > 42) {
			throw new BadRequestException(messageSource.getMessage(
					"exception.periodIncorrect", null, Locale.JAPAN));
		}

		// サイト空き状況検索
		List<SiteAvailability> result = siteAvailabilityService
				.findSiteAvailability(siteTypeId, startDate, endDate);
		if (result.isEmpty()) {
			throw new SystemException(messageSource.getMessage("exception.dataNotFound3",
					new String[] {String.valueOf(siteTypeId), startDate.toString(), endDate.toString()}, Locale.JAPAN));
		}

		return result.stream().map(element -> new ResultSiteAvailability(
				generateTitle(element.getAvailabilityCount())
				, element.getCalendarDate()
				, generateUrl(element.getAvailabilityCount()
				, element.getSiteTypeId()
				, element.getCalendarDate())))
				.collect(Collectors.toList());
	}

	/**
	 * スケジュール（FullCalendar）のイベントタイトル
	 * @param availabilityCount サイトの空き数
	 * @return title
	 */
	private String generateTitle(int availabilityCount) {
		if (availabilityCount == 0) {return "×";}
		else if (availabilityCount < 3) {return "△";}
		else {return "◎";}
	}

	/**
	 * スケジュール（FullCalendar）のイベントURL
	 * @param availabilityCount	サイトの空き数
	 * @param siteTypeId	サイトタイプID
	 * @param dateFrom	チェックイン日
	 * @return url
	 */
	private String generateUrl(int availabilityCount, int siteTypeId, LocalDate dateFrom) {
		String url =ServletUriComponentsBuilder.fromCurrentContextPath().path("/camping/stayInfo")
				.queryParam("form")
				.queryParam("siteTypeId", siteTypeId)
				.queryParam("dateFrom", dateFrom.format(ISO_LOCAL_DATE))
				.toUriString();
		return availabilityCount == 0 ? "" : url;
	}

}
