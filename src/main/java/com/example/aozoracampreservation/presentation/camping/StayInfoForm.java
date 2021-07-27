package com.example.aozoracampreservation.presentation.camping;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 宿泊情報フォーム
 */
@Data
public class StayInfoForm implements Serializable {

	@NotNull
	private int siteTypeId;

	@NotNull
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate dateFrom;

	@NotNull
	@Min(1)
	@Max(10)
	private int stayDays;

	@NotNull
	@Min(1)
	private int numberOfPeople;

	@NotBlank
	private String siteTypeName;

}
