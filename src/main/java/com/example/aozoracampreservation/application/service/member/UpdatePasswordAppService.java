package com.example.aozoracampreservation.application.service.member;

import com.example.aozoracampreservation.domain.service.MemberService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * パスワード変更 Application Service
 */
@Service
public class UpdatePasswordAppService {

	private final MemberService memberService;

	public UpdatePasswordAppService(MemberService memberService) {
		this.memberService = memberService;
	}

	/**
	 * パスワード変更
	 * @param memberId 会員ID
	 * @param password 変更後パスワード
	 */
	@Transactional(rollbackFor = Exception.class)
	@PreAuthorize("!principal.username.equals('test@co.jp')")
	public void updatePassword(int memberId, String password) {
		memberService.updatePassword(memberId, password);
	}
}
