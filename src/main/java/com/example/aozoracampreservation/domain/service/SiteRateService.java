package com.example.aozoracampreservation.domain.service;

import com.example.aozoracampreservation.domain.model.SiteRate;
import com.example.aozoracampreservation.domain.model.SiteRatePivot;
import com.example.aozoracampreservation.repository.SiteRateMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * サイト料金 Domain Service
 */
@Service
public class SiteRateService {

	private final SiteRateMapper mapper;

	public SiteRateService(SiteRateMapper mapper) {
		this.mapper = mapper;
	}

	/**
	 * サイト料金取得
	 * @param siteTypeId サイトタイプID
	 * @param date 指定日
	 * @return サイト料金
	 */
	@Transactional(readOnly = true)
	public SiteRate findSiteRate(int siteTypeId, LocalDate date) {
		return mapper.findSiteRate(siteTypeId, date);
	}

	/**
	 * サイト料金表取得
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<SiteRatePivot> findAllSiteRatesPivot() {
		return mapper.findAllSiteRatesPivot(LocalDate.now());
	}
}
