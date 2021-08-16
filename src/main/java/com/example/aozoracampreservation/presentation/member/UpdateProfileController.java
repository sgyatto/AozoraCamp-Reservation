package com.example.aozoracampreservation.presentation.member;

import com.example.aozoracampreservation.application.service.member.UpdateProfileAppService;
import com.example.aozoracampreservation.domain.model.Member;
import com.example.aozoracampreservation.security.AuthenticatedMember;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 会員基本情報更新 Controller
 */
@Controller
@RequestMapping("/member/profile")
public class UpdateProfileController {

	private final UpdateProfileAppService updateProfileAppService;
	private final ModelMapper modelMapper;

	public UpdateProfileController(UpdateProfileAppService updateProfileAppService, ModelMapper modelMapper) {
		this.updateProfileAppService = updateProfileAppService;
		this.modelMapper = modelMapper;
	}

	@ModelAttribute("updateProfileForm")
	public UpdateProfileForm setUpUpdateProfileForm() {
		return new UpdateProfileForm();
	}

	/**
	 * 会員基本情報更新フォーム表示
	 */
	@GetMapping(params = "form")
	public String form(@AuthenticationPrincipal AuthenticatedMember authenticatedMember,
						 @ModelAttribute("updateProfileForm") UpdateProfileForm form) {
		// 会員情報取得
		Member member = updateProfileAppService.findMember(authenticatedMember.getId());
		modelMapper.map(member, form);
		return "member/profile/profile";
	}

	/**
	 * 会員基本情報更新
	 */
	@PostMapping()
	public String update(@AuthenticationPrincipal AuthenticatedMember authenticatedMember,
						@ModelAttribute("updateProfileForm") @Validated UpdateProfileForm form,
						BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return "member/profile/profile";
		}

		Member newMember = modelMapper.map(form, Member.class);
		// 会員基本情報更新
		updateProfileAppService.update(authenticatedMember.getId(), newMember);
		return "redirect:/member/profile?complete";
	}

	/**
	 * 会員基本情報更新完了画面表示
	 */
	@GetMapping(params = "complete")
	public String compete() {
		return "member/profile/updateComplete";
	}
}
