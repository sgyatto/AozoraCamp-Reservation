package com.example.aozoracampreservation.presentation.camping;

import com.example.aozoracampreservation.application.service.camping.CampingAppService;
import com.example.aozoracampreservation.application.service.camping.ReserveAppService;
import com.example.aozoracampreservation.domain.model.*;
import com.example.aozoracampreservation.exception.BusinessException;
import com.example.aozoracampreservation.exception.SystemException;
import com.example.aozoracampreservation.security.AuthenticatedMember;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Locale;

/**
 * キャンプ情報表示・予約 Controller
 */
@Controller
@RequestMapping("/camping")
public class CampingController {

	private final CampingAppService campingAppService;
	private final ReserveAppService reserveAppService;
	private final ModelMapper modelMapper;
	private final StayInfoFormValidator stayInfoFormValidator;
	private final MessageSource messageSource;

	public CampingController(CampingAppService campingAppService,
							 ReserveAppService reserveAppService,
							 ModelMapper modelMapper,
							 StayInfoFormValidator stayInfoFormValidator,
							 MessageSource messageSource) {
		this.campingAppService = campingAppService;
		this.reserveAppService = reserveAppService;
		this.modelMapper = modelMapper;
		this.stayInfoFormValidator = stayInfoFormValidator;
		this.messageSource = messageSource;
	}


	@InitBinder("stayInfoForm")
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(stayInfoFormValidator);
	}

	@ModelAttribute(value = "stayInfoForm")
	public StayInfoForm setUpStayInfoForm() {
		return new StayInfoForm();
	}

	@ModelAttribute(value = "userInfoForm")
	public UserInfoForm setUpUserInfoForm() {
		return new UserInfoForm();
	}


	/**
	 * サイトタイプ一覧表示
	 */
	@GetMapping("/siteTypes")
	public String siteTypeList(Model model) {
		// サイトタイプ一覧全件取得
		List<SiteType> siteTypes = campingAppService.findAllSiteType();
		model.addAttribute("siteTypeList", siteTypes);
		return "camping/siteTypes";
	}

	/**
	 * スケジュール表示
	 */
	@GetMapping(value = "/schedule", params = "siteTypeId")
	public String schedule(@RequestParam("siteTypeId") int siteTypeId, Model model) {
		// サイトタイプ名取得
		String siteTypeName = campingAppService.findSiteTypeName(siteTypeId);
		model.addAttribute("siteTypeId", siteTypeId);
		model.addAttribute("siteTypeName", siteTypeName);
		return "camping/schedule";
	}

	/**
	 * 宿泊情報フォーム表示
	 */
	@RequestMapping(value = "/stayInfo", params = {"form", "siteTypeId", "dateFrom"},
					method = {RequestMethod.GET, RequestMethod.POST})
	public String stayInfo(@ModelAttribute("stayInfoForm") StayInfoForm form) {
		// サイトタイプ名取得
		String siteTypeName = campingAppService.findSiteTypeName(form.getSiteTypeId());
		form.setSiteTypeName(siteTypeName);
		return "camping/stayInfo";
	}

	/**
	 * 宿泊情報を予約内容確認（会員）に引き継ぎ
	 */
	@PostMapping(value = "/stayInfo", params = "member")
	public String sendToReserve(@ModelAttribute("stayInfoForm") @Validated StayInfoForm form,
								BindingResult bindingResult, HttpSession session) {

		if (bindingResult.hasErrors()) {
			return "camping/stayInfo";
		}
		// セッション設定（リダイレクト後のリロード対策）
		session.setAttribute("stayInfoFormSession", form);
		// 予約内容確認（会員）は要認証
		return "redirect:/camping/member/reserve?confirm";
	}

	/**
	 * 宿泊情報を予約者情報フォーム表示（非会員）に引き継ぎ
	 */
	@PostMapping(value = "/stayInfo", params = "guest")
	public String sendToGuestReserve(@ModelAttribute("stayInfoForm") @Validated StayInfoForm form,
								BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return "camping/stayInfo";
		}
		return "forward:/camping/guest/reserve?form";
	}


	//------------------------ Member Reservation -----------------------------

	/**
	 * 予約内容確認（会員）
	 */
	@GetMapping(value = "/member/reserve", params = "confirm")
	public String confirmByMember(@AuthenticationPrincipal AuthenticatedMember authenticatedMember,
								  Model model, HttpSession session) {
		// セッションから宿泊情報取得
		StayInfoForm form = (StayInfoForm) session.getAttribute("stayInfoFormSession");
		StayInfo stayInfo = modelMapper.map(form, StayInfo.class);
		// 会員情報取得
		Member member = reserveAppService.findMemberById(authenticatedMember.getId());

		// 予約情報取得
		UserInfo userInfo = new UserInfo();
		userInfo.setId(member.getId());
		Reservation reservation = reserveAppService.buildReservation(stayInfo, userInfo);

		UserInfoForm userInfoForm = modelMapper.map(member, UserInfoForm.class);
		model.addAttribute("guestFlg", false);
		model.addAttribute("reservation", reservation);
		model.addAttribute("userInfo", userInfoForm);
		model.addAttribute("stayInfoForm", form);
		return "camping/confirm";
	}

	/**
	 * 予約（会員）
	 */
	@PostMapping(value = "/member/reserve")
	public String reserveByMember(@AuthenticationPrincipal AuthenticatedMember authenticatedMember,
								  @ModelAttribute("stayInfoForm") @Validated StayInfoForm form,
								  BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			// サイトの空きがない場合はフラグが立つ
			boolean outOfStockFlg = bindingResult.getFieldErrors()
						.stream()
						.anyMatch(e -> e.getCode().equals("validation.custom.siteIsNotAvailable"));
			if (outOfStockFlg) {
				throw new BusinessException(messageSource.getMessage(
						"exception.siteIsNotAvailable", null, Locale.JAPAN));
			}
			throw new SystemException(messageSource.getMessage("exception.errorAtCreate", null, Locale.JAPAN));
		}

		StayInfo stayInfo = modelMapper.map(form, StayInfo.class);

		// ユーザー情報設定
		UserInfo userInfo = new UserInfo();
		userInfo.setId(authenticatedMember.getId());
		Reservation reservation = reserveAppService.buildReservation(stayInfo, userInfo);

		// 予約
		reserveAppService.saveReservation(reservation);
		return "redirect:/camping/member/reserve?complete";
	}

	/**
	 * 予約完了画面表示（会員）
	 */
	@GetMapping(value = "/member/reserve", params = "complete")
	public String completeByMember() {
		return "camping/complete";
	}

	/**
	 * 宿泊情報フォームに戻る（会員）
	 */
	@PostMapping(value = "/member/reserve", params = "redoSiteInfo")
	public String redoStayInfoByMember(@ModelAttribute("stayInfoForm") StayInfoForm form,
							   			HttpSession session) {
		session.removeAttribute("stayInfoFormSession");
		return "forward:/camping/stayInfo?form";
	}

	//------------------------ Guest Reservation -----------------------------

	/**
	 * 予約者情報フォーム表示（非会員）
	 */
	@PostMapping(value = "/guest/reserve", params = "form")
	public String userInfoByGuest(@ModelAttribute("stayInfoForm") StayInfoForm stayInfoForm,
						   @ModelAttribute("userInfoForm") UserInfoForm userInfoForm) {
		return "camping/userInfo";
	}

	/**
	 * 予約内容確認（非会員）
	 */
	@PostMapping(value = "/guest/reserve", params = "confirm")
	public String confirmByGuest(@ModelAttribute("stayInfoForm") StayInfoForm stayInfoForm,
								 @ModelAttribute("userInfoForm") @Validated UserInfoForm userInfoForm,
								 BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			return "camping/userInfo";
		}

		StayInfo stayInfo = modelMapper.map(stayInfoForm, StayInfo.class);
		// 予約内容確認情報取得
		UserInfo userInfo = modelMapper.map(userInfoForm, UserInfo.class);
		Reservation reservation = reserveAppService.buildReservation(stayInfo, userInfo);

		model.addAttribute("guestFlg", true);
		model.addAttribute("reservation", reservation);
		model.addAttribute("userInfo", userInfoForm);
		return "camping/confirm";
	}

	/**
	 * 予約（非会員）
	 */
	@PostMapping(value = "/guest/reserve")
	public String reserveByGuest(@ModelAttribute("stayInfoForm") @Validated StayInfoForm stayInfoForm,
						  BindingResult bindingResult1,
						  @ModelAttribute("userInfoForm") @Validated UserInfoForm userInfoForm,
						  BindingResult bindingResult2) {

		if (bindingResult1.hasErrors() || bindingResult2.hasErrors()) {
			// サイトの空きがない場合はフラグが立つ
			boolean outOfStockFlg = bindingResult1.getFieldErrors()
					.stream()
					.anyMatch(e -> e.getCode().equals("validation.custom.siteIsNotAvailable"));
			if (outOfStockFlg) {
				throw new BusinessException(messageSource.getMessage(
						"exception.siteIsNotAvailable", null, Locale.JAPAN));
			}
			throw new SystemException(messageSource.getMessage("exception.errorAtCreate", null, Locale.JAPAN));
		}

		// 予約
		StayInfo stayInfo = modelMapper.map(stayInfoForm, StayInfo.class);
		UserInfo userInfo = modelMapper.map(userInfoForm, UserInfo.class);
		Reservation reservation = reserveAppService.buildReservation(stayInfo, userInfo);
		reserveAppService.saveReservation(reservation);
		return "redirect:/camping/guest/reserve?complete";
	}

	/**
	 * 予約完了画面表示（非会員）
	 */
	@GetMapping(value = "/guest/reserve", params = "complete")
	public String completeByGuest() {
		return "camping/complete";
	}

	/**
	 * 宿泊情報フォームに戻る（非会員）
	 */
	@PostMapping(value = "/guest/reserve", params = "redoSiteInfo")
	public String redoStayInfoByGuest(@ModelAttribute("stayInfoForm") StayInfoForm form) {
		return "forward:/camping/stayInfo?form";
	}

	/**
	 * 予約者情報フォームに戻る（会員）
	 */
	@PostMapping(value = "/guest/reserve", params = "redoUserInfo")
	public String redoUserInfoByGuest(@ModelAttribute("stayInfoForm") StayInfoForm stayInfoForm,
									  @ModelAttribute("userInfoForm") UserInfoForm userInfoForm) {

		return "camping/userInfo";
	}


	//------------------------ Exception Handler -----------------------------

	@ExceptionHandler(BusinessException.class)
	public String dataAccessExceptionHandler(BusinessException e, Model model) {
		model.addAttribute("message", e.getMessage());
		model.addAttribute("nextUrl", "/");
		model.addAttribute("nextViewName", "トップページ");
		return "error/uc_error";
	}

	@ExceptionHandler(DataAccessException.class)
	public String dataAccessExceptionHandler(Exception e, Model model) {
		model.addAttribute("message", messageSource.getMessage("exception.errorAtCreate", null, Locale.JAPAN));
		model.addAttribute("nextUrl", "/");
		model.addAttribute("nextViewName", "トップページ");
		return "error/uc_error";
	}
}
