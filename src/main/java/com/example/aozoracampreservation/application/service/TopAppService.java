package com.example.aozoracampreservation.application.service;

import com.example.aozoracampreservation.domain.model.SiteRatePivot;
import com.example.aozoracampreservation.domain.service.SiteRateService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * トップページ Application Service
 */
@Service
public class TopAppService {

	private final SiteRateService siteRateService;

	public TopAppService(SiteRateService siteRateService) {
		this.siteRateService = siteRateService;
	}

	/**
	 * サイトタイプ別料金情報取得
	 * @return
	 */
	public List<SiteRatePivot> findAllSiteRatesPivot() {
		return siteRateService.findAllSiteRatesPivot();
	}

}
