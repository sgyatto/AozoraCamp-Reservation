package com.example.aozoracampreservation.repository;

import com.example.aozoracampreservation.domain.model.ReservationDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReservationDetailMapper {

	int createDetails(@Param("details") List<ReservationDetail> details,
					  @Param("reservationId") int reservationId);
}
