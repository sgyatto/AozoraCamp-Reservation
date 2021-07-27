package com.example.aozoracampreservation.presentation.signup;

import com.example.aozoracampreservation.domain.service.MemberService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 会員登録フォーム Validator
 */
@Component
public class SignupFormValidator implements Validator {

	private final MemberService memberService;

	public SignupFormValidator(MemberService memberService) {
		this.memberService = memberService;
	}


	@Override
	public boolean supports(Class<?> clazz) {
		return SignupForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		SignupForm form = (SignupForm) target;
		// メールアドレスの重複検証
		validateDuplicateMail(errors,form.getMail());
	}

	/**
	 * メールアドレスの重複検証<br>
	 * メールアドレスが既に会員登録されている場合はエラー。
	 * @param errors Errors
	 * @param mail メールアドレス
	 */
	private void validateDuplicateMail(Errors errors, String mail) {
		if (memberService.findByMail(mail).isPresent()) {
			errors.rejectValue("mail"	,"validation.custom.duplicateMail");
		}
	}
}
