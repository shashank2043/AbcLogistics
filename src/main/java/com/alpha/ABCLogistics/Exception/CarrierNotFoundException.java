package com.alpha.ABCLogistics.Exception;

public class CarrierNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public CarrierNotFoundException(String message) {
		super(message);
	}
	public CarrierNotFoundException() {
		super();
	}
}
