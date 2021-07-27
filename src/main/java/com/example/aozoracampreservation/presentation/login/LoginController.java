package com.example.aozoracampreservation.presentation.login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * ログイン Controller
 */
@Slf4j
@Controller
public class LoginController {

	private static final String GUEST_MEMBER_MAIL = "aozora@camp.com";
	private static final String GUEST_MEMBER_PASS = "password";

	/**
	 * ログイン
	 */
	@GetMapping("/login")
	public String login() {
		return "login/login";
	}

	/**
	 * ゲストログイン
	 */
	@GetMapping("/login/guest")
	public String guestLogin(HttpServletRequest request, HttpSession session) throws ServletException {
		// ゲストメンバーでログイン
		request.login(GUEST_MEMBER_MAIL, GUEST_MEMBER_PASS);
		log.info("User logged in. id={}", GUEST_MEMBER_MAIL);

		// 予約内容確認（会員）へのリダイレクト対応
		SavedRequest savedRequest = (SavedRequest) session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
		if (savedRequest != null) {
			// 認証成功後のリダイレクト先
			String redirectUrl = savedRequest.getRedirectUrl();
			if (redirectUrl.contains("/camping/member/reserve")) {
				return "redirect:/camping/member/reserve?confirm";
			}
		}
		return "redirect:/";
	}
}
