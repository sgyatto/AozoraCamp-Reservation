package com.example.aozoracampreservation.domain.service;

import com.example.aozoracampreservation.domain.model.SiteAvailability;
import com.example.aozoracampreservation.infrastructure.repository.SiteAvailabilityMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * サイト空き状況 Domain Service
 */
@Service
public class SiteAvailabilityService {

	private final SiteAvailabilityMapper mapper;

	public SiteAvailabilityService(SiteAvailabilityMapper mapper) {
		this.mapper = mapper;
	}

	/**
	 * サイト空き状況検索
	 * @param siteTypeId
	 * @param from
	 * @param to
	 * @return サイト空き状況 List
	 */
	@Transactional(readOnly = true)
	public List<SiteAvailability> findSiteAvailability(
			int siteTypeId, LocalDate from, LocalDate to) {
		return mapper.findSiteAvailability(siteTypeId, from, to);
	}

	/**
	 * サイト空き状況判定<br>
	 * 指定した宿泊期間のサイト空き状況が満室の場合はfalse、それ以外はtrueを返す。
	 * @param siteTypeId	サイトタイプID
	 * @param from	チェックイン日
	 * @param to	最終宿泊日
	 * @return 判定値
	 */
	@Transactional(readOnly = true)
	public boolean isSiteAvailableForPeriod(int siteTypeId, LocalDate from, LocalDate to) {
		return mapper.countDaysNotAvailable(siteTypeId, from, to) != 0;
	}

	/**
	 * サイト空き状況減算処理<br>
	 * 指定した宿泊期間のサイト空き状況に対し、在庫を1減らす。
	 * @param siteTypeId サイトタイプID
	 * @param from チェックイン日
	 * @param to 最終宿泊日
	 * @return 減算した数
	 */
	@Transactional(rollbackFor = Exception.class)
	public int reduceAvailabilityCount(int siteTypeId, LocalDate from, LocalDate to) {
		return mapper.reduceAvailabilityCount(siteTypeId, from, to);
	}
}
