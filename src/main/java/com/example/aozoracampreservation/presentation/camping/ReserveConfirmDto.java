package com.example.aozoracampreservation.presentation.camping;

import com.example.aozoracampreservation.domain.model.ReservationDetail;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 予約内容確認情報
 */
@Data
public class ReserveConfirmDto {

	private BigDecimal totalAmountTaxIncl;
	private BigDecimal salesTax;
	private List<ReservationDetail> reservationDetails;

}
