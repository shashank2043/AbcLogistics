package com.alpha.ABCLogistics.DTO;

import jakarta.validation.constraints.NotNull;

public class LoadingDto {
	@NotNull
	private String date;
	@NotNull
	private String time;
	public LoadingDto(String date, String time) {
		super();
		this.date = date;
		this.time = time;
	}
	public LoadingDto() {
		super();
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
