package com.example.aozoracampreservation.application.service.member;

import com.example.aozoracampreservation.domain.model.Member;
import com.example.aozoracampreservation.domain.service.MemberService;
import com.example.aozoracampreservation.exception.SystemException;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

/**
 * 会員基本情報更新 Application Service
 */
@Service
public class UpdateProfileAppService {

	private final MemberService memberService;
	private final MessageSource messageSource;

	public UpdateProfileAppService(MemberService memberService, MessageSource messageSource) {
		this.memberService = memberService;
		this.messageSource = messageSource;
	}

	/**
	 * 会員情報取得
	 * @param memberId 会員ID
	 * @return 会員
	 */
	public Member findMember(int memberId) {
		return memberService.findById(memberId)
				.orElseThrow(() -> new SystemException(messageSource.getMessage("exception.dataNotFound",
						new String[] {String.valueOf(memberId)}, Locale.JAPAN)));
	}

	/**
	 * 会員基本情報更新
	 * @param memberId 会員ID
	 * @param newMember 更新後会員情報
	 */
	@Transactional(rollbackFor = Exception.class)
	public void update(int memberId, Member newMember) {
		Member member = findMember(memberId);
		// 名前設定
		member.setName(newMember.getName());
		// 電話番号設定
		member.setPhoneNumber(newMember.getPhoneNumber());
		memberService.update(member);
	}
}
