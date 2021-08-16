package com.example.aozoracampreservation.security;

import com.example.aozoracampreservation.domain.model.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 認証会員
 */
public class AuthenticatedMember implements UserDetails {

	private final int id;
	private final String mail;
	private final String password;
	private final String role;

	public AuthenticatedMember(Member member) {
		this.id = member.getId();
		this.mail = member.getMail();
		this.password = member.getPassword();
		this.role = member.getRole();
	}

	public int getId() {
		return id;
	}

	public String getMail() {
		return mail;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList(role);
	}

	@Override
	public String getPassword() {
		return password;
	}

	/**
	 * 認証で使用したログインID（メールアドレス）を取得
	 * @return ログインID（メールアドレス）
	 */
	@Override
	public String getUsername() {
		return mail;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
