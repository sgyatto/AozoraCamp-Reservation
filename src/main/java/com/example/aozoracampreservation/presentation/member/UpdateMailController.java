package com.example.aozoracampreservation.presentation.member;

import com.example.aozoracampreservation.application.service.member.UpdateMailAppService;
import com.example.aozoracampreservation.domain.model.Member;
import com.example.aozoracampreservation.security.user_details.AuthenticatedMember;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/**
 * メールアドレス変更 Controller
 */
@Controller
@RequestMapping("/member/mail")
public class UpdateMailController {

	private final UpdateMailAppService updateMailAppService;
	private final UpdateMailFormValidator updateMailFormValidator;

	public UpdateMailController(UpdateMailAppService updateMailAppService,
								UpdateMailFormValidator updateMailFormValidator) {
		this.updateMailAppService = updateMailAppService;
		this.updateMailFormValidator = updateMailFormValidator;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(updateMailFormValidator);
	}

	@ModelAttribute("updateMailForm")
	public UpdateMailForm setUpUpdateMailForm() {
		return new UpdateMailForm();
	}

	/**
	 * メールアドレス変更フォーム表示
	 */
	@GetMapping(params = "form")
	public String form(@AuthenticationPrincipal AuthenticatedMember authenticatedMember, Model model) {
		model.addAttribute("currentMail", authenticatedMember.getMail());
		return "member/mail/mail";
	}

	/**
	 * メールアドレス変更
	 */
	@PostMapping()
	public String update(@AuthenticationPrincipal AuthenticatedMember authenticatedMember,
						@ModelAttribute("updateMailForm") @Validated UpdateMailForm form,
						BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return form(authenticatedMember, model);
		}
		// メールアドレス変更
		updateMailAppService.update(authenticatedMember.getId(), form.getMail());
		return "redirect:/member/mail?complete";
	}

	/**
	 * メールアドレス変更完了画面表示
	 */
	@GetMapping(params = "complete")
	public String complete() {
		return "member/mail/updateComplete";
	}
}
