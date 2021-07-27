package com.example.aozoracampreservation.application.service.member;

import com.example.aozoracampreservation.domain.model.Reservation;
import com.example.aozoracampreservation.domain.service.ReservationService;
import com.example.aozoracampreservation.exception.BusinessException;
import com.example.aozoracampreservation.exception.SystemException;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * 予約内容の確認・キャンセル Application Service
 */
@Service
public class ReservationsAppService {

	private final ReservationService reservationService;
	private final MessageSource messageSource;

	public ReservationsAppService(ReservationService reservationService, MessageSource messageSource) {
		this.reservationService = reservationService;
		this.messageSource = messageSource;
	}

	/**
	 * 予約検索（ページネーション）
	 * @param memberId 会員ID
	 * @param pageable ページ情報
	 * @return 予約リスト（指定ページ）
	 */
	public Page<Reservation> searchReservations(int memberId, Pageable pageable) {
		return reservationService.searchReservations(memberId, pageable);
	}

	/**
	 * 予約詳細取得
	 * @param reservationId 予約ID
	 * @return 予約（予約詳細含む）
	 */
	@PostAuthorize("principal.username.equals(returnObject.member.mail)")
	public Reservation findReservationDetails(int reservationId) {
		return reservationService.findReservationDetailsById(reservationId)
				.orElseThrow(() -> new SystemException(messageSource.getMessage("exception.dataNotFound",
						new String[] {String.valueOf(reservationId)}, Locale.JAPAN)));
	}

	/**
	 * 予約キャンセル
	 * @param reservationId 予約ID
	 * @return キャンセル件数
	 */
	public int cancelReservation(int reservationId) {
		// 権限確認
		Reservation reservation = this.findReservationDetails(reservationId);
		if (reservation.isCanceled() || !reservation.canCancel()) {
			// キャンセル済みまたはキャンセル期限を過ぎている場合はエラー
			throw new BusinessException(messageSource.getMessage("exception.cannotCancel", null, Locale.JAPAN));
		}
		// 予約キャンセル
		return reservationService.cancelReservation(reservationId);
	}
}
