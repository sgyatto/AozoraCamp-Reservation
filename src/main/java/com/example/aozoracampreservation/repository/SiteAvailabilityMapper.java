package com.example.aozoracampreservation.repository;

import com.example.aozoracampreservation.domain.model.SiteAvailability;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface SiteAvailabilityMapper {

	List<SiteAvailability> findSiteAvailability(int siteTypeId, LocalDate from, LocalDate to);

	int countDaysNotAvailable(int siteTypeId, LocalDate from, LocalDate to);

	int reduceAvailabilityCount(int siteTypeId, LocalDate from, LocalDate to);
}
