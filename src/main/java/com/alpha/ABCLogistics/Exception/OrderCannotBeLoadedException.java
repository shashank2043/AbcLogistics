package com.alpha.ABCLogistics.Exception;

public class OrderCannotBeLoadedException extends RuntimeException {

	public OrderCannotBeLoadedException() {
		super();
	}

	public OrderCannotBeLoadedException(String message) {
		super(message);
	}

}
