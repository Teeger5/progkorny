package hu.nye.spring.core.exceptions;

/**
 * Ez az osztály azokhoz a kivételekhez van,
 * amelyeknél Unprocessable Entity választ kell küldeni
 */
public class UnprocessableEntityException extends RuntimeException {
	public UnprocessableEntityException(String message) {
		super(message);
	}
}
