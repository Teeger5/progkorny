package hu.nye.spring.core.exceptions;

public class MCServerNotFoundException extends NotFoundException {
	/*
	 * Konstruktor
	 * @param uniqueId a szerver címe (address) vagy ID
	 * @param idType az azonosító típusa az üzenetben
	 */
	public MCServerNotFoundException(String address) {
		super(String.format("A '%s' címem nem található szerver az adatbázisban", address));
	}
}
