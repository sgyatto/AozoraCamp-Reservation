package com.example.aozoracampreservation.repository;

import com.example.aozoracampreservation.domain.model.SiteType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface SiteTypeMapper {

	List<SiteType> findAll();

	Optional<SiteType> findById(int id);
}
