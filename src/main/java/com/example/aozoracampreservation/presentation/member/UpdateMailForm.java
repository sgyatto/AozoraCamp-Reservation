package com.example.aozoracampreservation.presentation.member;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * メールアドレス変更フォーム
 */
@Data
public class UpdateMailForm {

	@NotBlank
	@Email
	private String mail;
}
