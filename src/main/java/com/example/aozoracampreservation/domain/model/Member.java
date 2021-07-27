package com.example.aozoracampreservation.domain.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会員
 */
@Data
public class Member {
	private int id;
	private String name;
	private String mail;
	private String password;
	private String phoneNumber;
	private String role;
	private LocalDateTime updatedAt;
	private LocalDateTime createdAt;

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


