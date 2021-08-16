package com.example.aozoracampreservation.domain.service;

import com.example.aozoracampreservation.domain.model.Member;
import com.example.aozoracampreservation.repository.MemberMapper;
import com.example.aozoracampreservation.security.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 会員 Domain Service
 */
@Service
public class MemberService {

	private final MemberMapper mapper;
	private final PasswordEncoder encoder;

	public MemberService(MemberMapper mapper, PasswordEncoder encoder) {
		this.mapper = mapper;
		this.encoder = encoder;
	}

	/**
	 * 会員情報取得
	 * @param mail メールアドレス
	 * @return
	 */
	@Transactional(readOnly = true)
	public Optional<Member> findByMail(String mail) {
		return mapper.findByMail(mail);
	}

	/**
	 * 会員情報取得
	 * @param memberId 会員ID
	 * @return
	 */
	@Transactional(readOnly = true)
	public Optional<Member> findById(int memberId) {
		return mapper.findById(memberId);
	}

	/**
	 * 会員登録
	 * @param member 会員情報
	 */
	@Transactional(rollbackFor = Exception.class)
	public void create(Member member) {
		member.setPassword(encoder.encode(member.getPassword()));
		member.setRole(Role.ROLE_GENERAL.toString());
		mapper.create(member);
	}

	/**
	 * 会員更新
	 * @param member 更新後会員情報
	 */
	@Transactional(rollbackFor = Exception.class)
	public void update(Member member) {
		mapper.update(member);
	}

	/**
	 * パスワード更新
	 * @param memberId 会員ID
	 * @param rowPassword 暗号化前パスワード
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updatePassword(int memberId, String rowPassword) {
		String encodedPassword = encoder.encode(rowPassword);
		mapper.updatePassword(memberId, encodedPassword);
	}
}
