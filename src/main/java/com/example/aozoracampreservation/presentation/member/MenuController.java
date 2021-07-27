package com.example.aozoracampreservation.presentation.member;

import com.example.aozoracampreservation.application.service.member.MenuAppService;
import com.example.aozoracampreservation.domain.model.Member;
import com.example.aozoracampreservation.security.user_details.AuthenticatedMember;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 会員メニュー Controller
 */
@Controller
public class MenuController {

	private final MenuAppService menuAppService;

	public MenuController(MenuAppService menuAppService) {
		this.menuAppService = menuAppService;
	}

	/**
	 * 会員メニュー表示
	 */
	@GetMapping("/member/menu")
	public String getMembersMenuPage(@AuthenticationPrincipal AuthenticatedMember authenticatedMember,
									 Model model) {
		// 会員情報取得
		Member member = menuAppService.findMember(authenticatedMember.getId());
		model.addAttribute("userName", member.getName());
		return "member/menu";
	}

}
