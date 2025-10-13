package com.alpha.ABCLogistics.Exception;

public class OrderCannotBeUnloadedException extends RuntimeException {

	public OrderCannotBeUnloadedException() {
		super();
	}

	public OrderCannotBeUnloadedException(String message) {
		super(message);
	}

}
