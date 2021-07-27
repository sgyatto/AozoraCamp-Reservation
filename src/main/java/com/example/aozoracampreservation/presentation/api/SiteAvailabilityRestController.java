package com.example.aozoracampreservation.presentation.api;

import com.example.aozoracampreservation.application.service.api.SiteAvailabilityRestAppService;
import org.springframework.web.bind.annotation.*;

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
	@GetMapping(value = "/siteTypes/{id}")
	public List<ResultSiteAvailability> fetchSiteAvailabilityForSchedule(
			@PathVariable("id") int siteTypeId) {
		return siteAvailabilityRestAppService.fetchSiteAvailabilityForSchedule(siteTypeId);
	}

}
