package com.example.aozoracampreservation.repository;

import com.example.aozoracampreservation.domain.model.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberMapper {

	Optional<Member> findByMail(String mail);

	Optional<Member> findById(int id);

	void create(Member member);

	void update(Member member);

	void updatePassword(int id, String encodedPassword);

}
