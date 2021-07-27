package com.example.aozoracampreservation.presentation.member;

import com.example.aozoracampreservation.application.service.member.UpdatePasswordAppService;
import com.example.aozoracampreservation.security.user_details.AuthenticatedMember;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/**
 * パスワード変更 Controller
 */
@Controller
@RequestMapping("/member/password")
public class UpdatePasswordController {

	private final UpdatePasswordAppService updatePasswordAppService;
	private final UpdatePasswordFormValidator updatePasswordFormValidator;

	public UpdatePasswordController(UpdatePasswordAppService updatePasswordAppService,
									UpdatePasswordFormValidator updatePasswordFormValidator) {
		this.updatePasswordAppService = updatePasswordAppService;
		this.updatePasswordFormValidator = updatePasswordFormValidator;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(updatePasswordFormValidator);
	}

	@ModelAttribute("updatePasswordForm")
	public UpdatePasswordForm setUpPasswordForm() {
		return new UpdatePasswordForm();
	}

	/**
	 * パスワード変更フォーム表示
	 */
	@GetMapping(params = "form")
	public String form() {
		return "member/password/password";
	}

	/**
	 * パスワード変更
	 */
	@PostMapping()
	public String update(@AuthenticationPrincipal AuthenticatedMember authenticatedMember,
							@ModelAttribute("updatePasswordForm") @Validated UpdatePasswordForm form,
							BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "member/password/password";
		}
		// パスワード変更
		updatePasswordAppService.updatePassword(authenticatedMember.getId(), form.getPassword());
		return "redirect:/member/password?complete";
	}

	/**
	 * パスワード変更完了画面表示
	 */
	@GetMapping(params = "complete")
	public String complete() {
		return "member/password/updateComplete";
	}
}
