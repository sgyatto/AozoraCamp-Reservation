package com.example.aozoracampreservation.presentation.member;

import com.example.aozoracampreservation.domain.service.MemberService;
import com.example.aozoracampreservation.security.AuthenticatedMember;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * パスワード変更フォーム Validator
 */
@Component
public class UpdatePasswordFormValidator implements Validator {

	private final MemberService memberService;
	private final PasswordEncoder encoder;

	public UpdatePasswordFormValidator(MemberService memberService, PasswordEncoder encoder) {
		this.memberService = memberService;
		this.encoder = encoder;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return UpdatePasswordForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UpdatePasswordForm form = (UpdatePasswordForm) target;
		// 現在パスワード一致検証
		validateIsDifferentFromCurrentPassword(errors, form.getCurrentPassword());
	}

	/**
	 * 現在パスワード一致検証<br>
	 * 入力された現在のパスワードが登録内容と一致しない場合エラー。
	 * @param errors Errors
	 * @param currentPassword 入力された現在のパスワード
	 */
	private void validateIsDifferentFromCurrentPassword(Errors errors, String currentPassword) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!encoder.matches(currentPassword, ((AuthenticatedMember) auth.getPrincipal()).getPassword())) {
			errors.rejectValue("currentPassword"
					, "validation.custom.currentPasswordIncorrect");
		}
	}
}
