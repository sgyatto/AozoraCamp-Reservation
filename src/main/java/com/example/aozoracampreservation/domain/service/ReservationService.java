package com.example.aozoracampreservation.domain.service;

import com.example.aozoracampreservation.domain.model.Reservation;
import com.example.aozoracampreservation.repository.ReservationMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 予約 Domain Service
 */
@Service
public class ReservationService {

	private final ReservationMapper mapper;

	public ReservationService(ReservationMapper mapper) {
		this.mapper = mapper;
	}

	/**
	 * 予約登録
	 * @param reservation 予約
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public int createReservation(Reservation reservation) {
		return mapper.create(reservation);
	}

	/**
	 * 予約検索（ページネーション）
	 * @param memberId 会員ID
	 * @param pageable ページ情報
	 * @return 予約リスト（指定ページ）
	 */
	@Transactional(readOnly = true)
	public Page<Reservation> searchReservations(int memberId, Pageable pageable) {
		// 指定した会員の予約数取得
		int count = mapper.countByMemberId(memberId);
		List<Reservation> reservations;
		// 指定した会員の予約取得
		reservations = mapper.findPageByMemberId(memberId, pageable);
		return new PageImpl<>(reservations, pageable, count);
	}

	/**
	 * 予約詳細取得
	 * @param reservationId 予約ID
	 * @return 予約（予約詳細含む）
	 */
	@Transactional(readOnly = true)
	public Optional<Reservation> findReservationDetailsById(int reservationId) {
		return mapper.findReservationDetailsById(reservationId);
	}

	/**
	 * 予約キャンセル
	 * @param reservationId 予約ID
	 * @return キャンセル件数
	 */
	@Transactional(rollbackFor = Exception.class)
	public int cancelReservation(int reservationId) {
		return mapper.cancelReservation(reservationId);
	}
}
