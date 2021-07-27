package com.example.aozoracampreservation.presentation.api;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

/**
 * サイト空き状況（スケジュール用）
 */
@Data
@AllArgsConstructor
public class ResultSiteAvailability {

	private String title;

	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate start;

	private String url;

}
