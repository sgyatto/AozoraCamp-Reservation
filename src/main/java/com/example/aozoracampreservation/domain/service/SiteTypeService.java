package com.example.aozoracampreservation.domain.service;

import com.example.aozoracampreservation.domain.model.SiteType;
import com.example.aozoracampreservation.repository.SiteTypeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * サイトタイプ Domain Service
 */
@Service
public class SiteTypeService {

	private final SiteTypeMapper mapper;

	public SiteTypeService(SiteTypeMapper mapper) {
		this.mapper = mapper;
	}

	/**
	 * サイトタイプ全件取得
	 * @return サイトタイプ全件
	 */
	@Transactional(readOnly = true)
	public List<SiteType> findAll() {
		return mapper.findAll();
	}

	/**
	 * サイトタイプ取得（ID指定）
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public Optional<SiteType> findById(int id) {
		return mapper.findById(id);
	}
}
