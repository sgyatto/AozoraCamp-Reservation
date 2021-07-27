package com.example.aozoracampreservation.presentation;

import com.example.aozoracampreservation.application.service.TopAppService;
import com.example.aozoracampreservation.domain.model.SiteRatePivot;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * トップページ Controller
 */
@Controller
public class TopController {

	private final TopAppService topAppService;

	public TopController(TopAppService topAppService) {
		this.topAppService = topAppService;
	}

	/**
	 * トップページ表示
	 */
	@GetMapping("/")
	public String top(Model model) {
		// サイトタイプ別料金情報取得
		List<SiteRatePivot> siteRates = topAppService.findAllSiteRatesPivot();
		model.addAttribute("siteRates", siteRates);
		return "index";
	}
}
