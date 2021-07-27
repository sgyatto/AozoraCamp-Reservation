package com.example.aozoracampreservation.presentation.camping;

import com.example.aozoracampreservation.domain.model.StayInfo;
import com.example.aozoracampreservation.domain.service.SiteAvailabilityService;
import com.example.aozoracampreservation.domain.service.SiteTypeService;
import com.example.aozoracampreservation.exception.SystemException;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Locale;


/**
 * 宿泊情報フォーム Validator
 */
@Component
public class StayInfoFormValidator implements Validator {


	private final SiteTypeService siteTypeService;
	private final SiteAvailabilityService siteAvailabilityService;
	private final ModelMapper modelMapper;
	private final MessageSource messageSource;

	public StayInfoFormValidator(SiteTypeService siteTypeService, SiteAvailabilityService siteAvailabilityService,
								 ModelMapper modelMapper, MessageSource messageSource) {
		this.siteTypeService = siteTypeService;
		this.siteAvailabilityService = siteAvailabilityService;
		this.modelMapper = modelMapper;
		this.messageSource = messageSource;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return StayInfoForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		StayInfoForm form = (StayInfoForm) target;
		StayInfo stayInfo = modelMapper.map(form, StayInfo.class);

		// サイトの上限人数取得
		int capacity = siteTypeService.findById(stayInfo.getSiteTypeId())
				.map(st -> st.getCapacity())
				.orElseThrow(() -> new SystemException(messageSource.getMessage("exception.dataNotFound",
									new String[] {String.valueOf(stayInfo.getSiteTypeId())}, Locale.JAPAN)));
		// 宿泊人数検証
		validateNumberOfPeople(errors, stayInfo, capacity);
		// 予約受付期間判定
		if (!validateOKPeriodOfStay(errors, stayInfo)) {
			return;
		}
		//サイト空き状況検証
		validateSiteAvailability(errors, stayInfo);
	}

	/**
	 * 宿泊人数検証<br>
	 * 宿泊人数がサイトの上限人数を上回る場合エラー。
	 * @param errors Errors
	 * @param stayInfo 宿泊情報
	 * @param capacity サイトの上限人数
	 */
	private void validateNumberOfPeople(Errors errors, StayInfo stayInfo, int capacity) {
		if (!stayInfo.isValidNumberOfPeople(capacity)) {
			errors.rejectValue("numberOfPeople", "validation.custom.numberOfPeopleIncorrect"
					, new String[] {String.valueOf(capacity)}, "宿泊人数が上限を超えています");
		}
	}

	/**
	 * 予約受付期間判定<br>
	 * 予約受付期間外の日程が含まれる場合はエラー情報を設定し、falseを返す。それ以外はtrueを返す。
	 * @param errors Errors
	 * @param stayInfo 宿泊情報
	 * @return 判定値
	 */
	private boolean validateOKPeriodOfStay(Errors errors, StayInfo stayInfo) {
		if (!stayInfo.isValidPeriod()) {
			errors.rejectValue("stayDays"	, "validation.custom.periodOfStayIncorrect");
			return false;
		}
		return true;
	}

	/**
	 * サイト空き状況検証<br>
	 * チェックイン日から最終宿泊日までの期間で、満室の日程が含まれる場合エラー。
	 * @param errors Errors
	 * @param stayInfo 宿泊情報
	 */
	private void validateSiteAvailability(Errors errors, StayInfo stayInfo) {
		if (siteAvailabilityService.isSiteAvailableForPeriod(
				stayInfo.getSiteTypeId(), stayInfo.getDateFrom(), stayInfo.getDateTo())) {
			errors.rejectValue("stayDays"	, "validation.custom.siteIsNotAvailable");
		}
	}

}
