package com.alpha.ABCLogistics.Exception;

public class TruckCapacityExceededException extends RuntimeException {

	public TruckCapacityExceededException(String message) {
		super(message);
	}

	public TruckCapacityExceededException() {
		super();
	}
}
