package com.example.aozoracampreservation.domain.service;

import com.example.aozoracampreservation.domain.model.ReservationDetail;
import com.example.aozoracampreservation.infrastructure.repository.ReservationDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 予約詳細 Domain Service
 */
@Service
public class ReservationDetailService {

	private final ReservationDetailMapper mapper;

	public ReservationDetailService(ReservationDetailMapper mapper) {
		this.mapper = mapper;
	}

	/**
	 * 予約詳細登録
	 * @param reservationDetails 予約詳細リスト
	 * @param reservationId 予約ID
	 * @return 登録件数
	 */
	@Transactional(rollbackFor = Exception.class)
	public int createReservationDetails(List<ReservationDetail> reservationDetails, int reservationId) {
		return mapper.createDetails(reservationDetails, reservationId);
	}
}
