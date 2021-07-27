package com.example.aozoracampreservation.presentation.member;

import com.example.aozoracampreservation.domain.model.Member;
import com.example.aozoracampreservation.domain.service.MemberService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Locale;
import java.util.Optional;

/**
 * メールアドレス変更フォーム Validator
 */
@Component
public class UpdateMailFormValidator implements Validator {

	private final MemberService memberService;

	public UpdateMailFormValidator(MemberService memberService) {
		this.memberService = memberService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return UpdateMailForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UpdateMailForm form = (UpdateMailForm) target;
		// メールアドレスの重複検証
		validateDuplicateMail(errors, form.getMail());
	}

	/**
	 * メールアドレスの重複検証<br>
	 * メールアドレスが既に会員登録されている場合はエラー。
	 * @param errors Errors
	 * @param formMail メールアドレス
	 */
	private void validateDuplicateMail(Errors errors, String formMail) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		// 現在の同じメールアドレス
		if (auth.getName().toLowerCase(Locale.ROOT).equals(formMail.toLowerCase(Locale.ROOT))) {
			return;
		}
		// メールアドレスが登録済み
		if (memberService.findByMail(formMail).isPresent()) {
			errors.rejectValue("mail"	, "validation.custom.duplicateMail");
		}
	}
}
