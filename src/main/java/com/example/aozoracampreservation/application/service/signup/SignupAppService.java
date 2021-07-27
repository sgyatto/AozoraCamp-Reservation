package com.example.aozoracampreservation.application.service.signup;

import com.example.aozoracampreservation.domain.model.Member;
import com.example.aozoracampreservation.domain.service.MemberService;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 会員登録 Application Service
 */
@Service
public class SignupAppService {

	private final MemberService memberService;

	public SignupAppService(MemberService memberService) {
		this.memberService = memberService;
	}

	/**
	 * 会員登録
	 * @param member 会員
	 */
	public void createMember(Member member) {
		memberService.create(member);
	}

	/**
	 * パスワードマスク
	 * @param password パスワード
	 * @return マスクされたパスワード
	 */
	public String maskPassword(String password) {
		return String.join("", Collections.nCopies(password.length(), "*"));
	}
}
