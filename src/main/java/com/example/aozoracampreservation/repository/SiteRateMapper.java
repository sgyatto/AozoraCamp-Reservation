package com.example.aozoracampreservation.repository;

import com.example.aozoracampreservation.domain.model.SiteRate;
import com.example.aozoracampreservation.domain.model.SiteRatePivot;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface SiteRateMapper {

	SiteRate findSiteRate(int siteTypeId, LocalDate date);

	List<SiteRatePivot> findAllSiteRatesPivot(LocalDate date);
}
