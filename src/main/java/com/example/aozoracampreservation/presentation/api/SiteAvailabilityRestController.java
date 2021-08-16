package com.example.aozoracampreservation.presentation.api;

import com.example.aozoracampreservation.application.service.api.SiteAvailabilityRestAppService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * サイト空き状況 Rest Controller
 */
@RestController
@RequestMapping("/api/schedule")
public class SiteAvailabilityRestController {

	private final SiteAvailabilityRestAppService siteAvailabilityRestAppService;

	public SiteAvailabilityRestController(SiteAvailabilityRestAppService siteAvailabilityRestAppService) {
		this.siteAvailabilityRestAppService = siteAvailabilityRestAppService;
	}

	/**
	 * サイト空き状況（スケジュール用）の検索
	 */
	@GetMapping(value = "/siteTypes/{id}", params = {"start", "end"})
	public List<ResultSiteAvailability> fetchSiteAvailabilityForSchedule(
			@PathVariable("id") int siteTypeId,
			@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		return siteAvailabilityRestAppService.fetchSiteAvailabilityForSchedule(siteTypeId, startDate, endDate);
	}

}
