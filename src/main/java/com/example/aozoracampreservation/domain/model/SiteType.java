package com.example.aozoracampreservation.domain.model;

import lombok.Data;

/**
 * サイトタイプ
 */
@Data
public class SiteType {
	private int id;
	private String name;
	private String listImgUrl;
	private int capacity;
}
