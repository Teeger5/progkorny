package hu.nye.spring.core.exceptions;

public class MCVersionNotFoundException extends NotFoundException {
	public MCVersionNotFoundException(String version) {
		super(String.format("A '%s' verzió nem található az adatbázisban", version));
	}
}
