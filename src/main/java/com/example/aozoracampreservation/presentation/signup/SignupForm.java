package com.example.aozoracampreservation.presentation.signup;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class SignupForm implements Serializable {

	@NotBlank(groups = ValidGroup.class)
	@Email(groups = ValidGroup.class)
	private String mail;

	@NotBlank(groups = ValidGroup.class)
	@Length(max = 100, message = "100桁以内で入力してください", groups = ValidGroup.class)
	private String name;

	@NotBlank(groups = ValidPassword.class)
	@Length(min = 8, message = "8桁以上で入力してください", groups = ValidPassword.class)
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "半角英数字で入力してください", groups = ValidPassword.class)
	private String password;

	@NotBlank(groups = ValidGroup.class)
	@Pattern(regexp = "^[0-9]+$", message = "半角数字で入力してください", groups = ValidGroup.class)
	@Length(max = 15, message = "15桁以内で入力してください", groups = ValidGroup.class)
	private String phoneNumber;

	public interface ValidGroup {}
	public interface ValidPassword {}
}
