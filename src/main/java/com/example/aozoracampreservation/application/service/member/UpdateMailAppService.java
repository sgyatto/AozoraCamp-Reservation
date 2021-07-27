package com.example.aozoracampreservation.application.service.member;

import com.example.aozoracampreservation.domain.model.Member;
import com.example.aozoracampreservation.domain.service.MemberService;
import com.example.aozoracampreservation.exception.SystemException;
import com.example.aozoracampreservation.security.user_details.AuthenticatedMember;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

/**
 * メールアドレス更新 Application Service
 */
@Service
public class UpdateMailAppService {

	private final MemberService memberService;
	private final MessageSource messageSource;

	public UpdateMailAppService(MemberService memberService, MessageSource messageSource) {
		this.memberService = memberService;
		this.messageSource = messageSource;
	}

	/**
	 * 会員情報取得
	 * @param memberId 会員ID
	 * @return 会員
	 */
	private Member findMember(int memberId) {
		return memberService.findById(memberId)
				.orElseThrow(() -> new SystemException(messageSource.getMessage("exception.dataNotFound",
						new String[] {String.valueOf(memberId)}, Locale.JAPAN)));
	}

	/**
	 * メールアドレス更新
	 * @param memberId 会員ID
	 * @param newMail 変更後メールアドレス
	 */
	@Transactional(rollbackFor = Exception.class)
	@PreAuthorize("!principal.username.equals('test@co.jp')")
	public void update(int memberId, String newMail) {
		Member member = findMember(memberId);
		member.setMail(newMail);
		memberService.update(member);
		this.updatePrincipalOfSecurityContext(member);
	}

	/**
	 * 認証情報更新
	 * @param updatedMember 更新後会員情報
	 */
	private void updatePrincipalOfSecurityContext(Member updatedMember) {
		AuthenticatedMember newAuthMember = new AuthenticatedMember(updatedMember);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// 更新後認証情報生成
		Authentication newAuth = new UsernamePasswordAuthenticationToken(
				newAuthMember, auth.getCredentials(), auth.getAuthorities());
		// 更新後認証情報設定
		SecurityContextHolder.getContext().setAuthentication(newAuth);
	}
}
