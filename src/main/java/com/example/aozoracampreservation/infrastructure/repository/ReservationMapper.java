package com.example.aozoracampreservation.infrastructure.repository;

import com.example.aozoracampreservation.domain.model.Reservation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ReservationMapper {

	int create(Reservation reservation);

	int countByMemberId(int memberId);

	List<Reservation> findPageByMemberId(@Param("memberId") int memberId, @Param("pageable") Pageable pageable);

	Optional<Reservation> findReservationDetailsById(int reservationId);

	int cancelReservation(int reservationId);
}
