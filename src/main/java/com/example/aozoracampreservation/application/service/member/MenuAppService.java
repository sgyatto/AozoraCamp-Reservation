package com.example.aozoracampreservation.application.service.member;

import com.example.aozoracampreservation.domain.model.Member;
import com.example.aozoracampreservation.domain.service.MemberService;
import com.example.aozoracampreservation.exception.SystemException;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * 会員メニュー Application Service
 */
@Service
public class MenuAppService {

	private final MemberService memberService;
	private final MessageSource messageSource;

	public MenuAppService(MemberService memberService, MessageSource messageSource) {
		this.memberService = memberService;
		this.messageSource = messageSource;
	}

	/**
	 * 会員情報取得
	 * @param memberId 会員ID
	 * @return 会員情報
	 */
	public Member findMember(int memberId) {
		return memberService.findById(memberId)
					.orElseThrow(() -> new SystemException(messageSource.getMessage("exception.dataNotFound",
							new String[] {String.valueOf(memberId)}, Locale.JAPAN)));
	}
}
