package com.example.aozoracampreservation.presentation.member;

import com.example.aozoracampreservation.presentation.signup.SignupForm;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 会員基本情報フォーム
 */
@Data
public class UpdateProfileForm {

	@NotBlank(groups = SignupForm.ValidGroup.class)
	@Length(max = 100, message = "100桁以内で入力してください")
	private String name;

	@NotBlank
	@Pattern(regexp = "^[0-9]+$", message = "半角数字で入力してください")
	@Length(max = 15, message = "15桁以内で入力してください")
	private String phoneNumber;
}
