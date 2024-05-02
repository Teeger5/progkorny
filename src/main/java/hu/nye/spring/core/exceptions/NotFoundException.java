package hu.nye.spring.core.exceptions;

/**
 * Ez az osztály összefoglalja azokat a kivételeket,
 * amelyekre 404 Not Found választ kell küldeni
 */
public class NotFoundException extends RuntimeException {
	public NotFoundException(String message) {
		super(message);
	}
}
