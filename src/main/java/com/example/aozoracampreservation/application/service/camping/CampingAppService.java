package com.example.aozoracampreservation.application.service.camping;

import com.example.aozoracampreservation.domain.model.SiteType;
import com.example.aozoracampreservation.domain.service.SiteTypeService;
import com.example.aozoracampreservation.exception.SystemException;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

/**
 * キャンプ情報表示 Application Service
 */
@Service
public class CampingAppService {

	private final SiteTypeService siteTypeService;
	private final MessageSource messageSource;

	public CampingAppService(SiteTypeService siteTypeService, MessageSource messageSource) {
		this.siteTypeService = siteTypeService;
		this.messageSource = messageSource;
	}

	/**
	 * サイトタイプ一覧全件取得
	 * @return サイトタイプ一覧
	 */
	public List<SiteType> findAllSiteType() {
		return siteTypeService.findAll();
	}

	/**
	 * サイトタイプ名取得
	 * @param siteTypeId サイトタイプID
	 * @return サイトタイプ名
	 */
	public String findSiteTypeName(int siteTypeId) {
		return siteTypeService.findById(siteTypeId)
				.map(st -> st.getName())
				.orElseThrow(() -> new SystemException(
						messageSource.getMessage("exception.dataNotFound",
								new String[] {String.valueOf(siteTypeId)}, Locale.JAPAN))
				);

	}
}
