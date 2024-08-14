package com.tmm.myre.base.exception;

public class ConverterException extends Exception {

	private static final long serialVersionUID = -3764527465864305140L;
	
	public ConverterException(final String mensajeFinal) {
		super(mensajeFinal);
	}
	
	public ConverterException(final String mensajeFinal, final Throwable causaFinal) {
		super(mensajeFinal, causaFinal);
	}
	
}