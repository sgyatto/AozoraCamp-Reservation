package com.example.aozoracampreservation.presentation.member;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * パスワード変更フォーム
 */
@Data
public class UpdatePasswordForm {

	@NotBlank
	@Length(min = 8, message = "8桁以上で入力してください")
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "半角英数字で入力してください")
	private String currentPassword;

	@NotBlank
	@Length(min = 8, message = "8桁以上で入力してください")
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "半角英数字で入力してください")
	private String password;
}
