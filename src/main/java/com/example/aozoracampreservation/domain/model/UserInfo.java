package com.example.aozoracampreservation.domain.model;

import lombok.Data;

/**
 * ユーザー情報
 */
@Data
public class UserInfo {

	private Integer id;
	private String name;
	private String mail;
	private String phoneNumber;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(50);
		sb.append(getClass().getSimpleName())
				.append("(id=")
				.append(this.id)
				.append(")");
		return sb.toString();
	}
}
