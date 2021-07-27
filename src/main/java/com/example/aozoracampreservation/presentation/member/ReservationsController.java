package com.example.aozoracampreservation.presentation.member;

import com.example.aozoracampreservation.application.service.member.ReservationsAppService;
import com.example.aozoracampreservation.domain.model.Reservation;
import com.example.aozoracampreservation.exception.BusinessException;
import com.example.aozoracampreservation.exception.SystemException;
import com.example.aozoracampreservation.security.user_details.AuthenticatedMember;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

/**
 * 予約内容の確認・キャンセル Controller
 */
@Controller
@RequestMapping("/member/reservations")
public class ReservationsController {

	private final ReservationsAppService reservationsAppService;
	private final MessageSource messageSource;

	public ReservationsController(ReservationsAppService reservationsAppService, MessageSource messageSource) {
		this.reservationsAppService = reservationsAppService;
		this.messageSource = messageSource;
	}

	/**
	 * 予約一覧
	 */
	@GetMapping("/list")
	public String list(@AuthenticationPrincipal AuthenticatedMember authenticatedMember,
					   @PageableDefault(size = 5) Pageable pageable, Model model) {
		// 予約検索（ページネーション）
		Page<Reservation> page = reservationsAppService.searchReservations(authenticatedMember.getId(), pageable);
		model.addAttribute("page", page);
		return "member/reservations/list";
	}

	/**
	 * 予約詳細
	 */
	@GetMapping(value = "/{id}", params = "page")
	public String detail(@PathVariable("id") int reservationId,
						 @RequestParam("page") int pageNumber, Model model) {
		// 予約詳細取得
		Reservation reservation = reservationsAppService.findReservationDetails(reservationId);
		model.addAttribute("reservation", reservation);
		model.addAttribute("pageNumber", pageNumber);
		return "member/reservations/detail";
	}

	/**
	 * 予約キャンセル
	 */
	@PostMapping(value = "/{id}/cancel")
	public String detail(@PathVariable("id") int reservationId) {
				// 予約キャンセル
		int canceledCount = reservationsAppService.cancelReservation(reservationId);
		if (canceledCount == 0) {
			throw new SystemException(messageSource.getMessage(
					"exception.errorAtCancel", null, Locale.JAPAN));
		}
		return "member/reservations/complete";
	}

	//------------------------ Exception Handler -----------------------------

	@ExceptionHandler(BusinessException.class)
	public String dataAccessExceptionHandler(BusinessException e, Model model) {
		model.addAttribute("message", e.getMessage());
		model.addAttribute("nextUrl", "/member/reservations/list");
		model.addAttribute("nextViewName", "予約一覧");
		return "error/uc_error";
	}
}
