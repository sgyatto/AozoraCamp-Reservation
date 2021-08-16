package com.example.aozoracampreservation.security;

import com.example.aozoracampreservation.domain.service.MemberService;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final MemberService memberService;
	private final MessageSource messageSource;

	public UserDetailsServiceImpl(MemberService memberService, MessageSource messageSource) {
		this.memberService = memberService;
		this.messageSource = messageSource;
	}


	@Override
	public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
		return memberService.findByMail(mail).map(AuthenticatedMember::new)
				.orElseThrow(() -> new UsernameNotFoundException(messageSource.getMessage(
						"exception.dataNotFound", new String[] {mail}, Locale.JAPAN)));
	}
}
