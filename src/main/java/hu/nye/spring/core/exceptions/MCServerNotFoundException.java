package hu.nye.spring.core.exceptions;

public class MCServerNotFoundException extends NotFoundException {
	/**
	 * Ezt a konstruktort cím esetén kell használni
	 * @param address a szerver címe
	 */
	public MCServerNotFoundException(String address) {
		super(String.format("A '%s' címem nem található szerver az adatbázisban", address));
	}

	/**
	 * Ezt a konstruktor ID esetén kell használni,
	 * Az üzenet általánosan fogalmaz
	 */
	public MCServerNotFoundException() {
		super("A szerver nem található az adatbázisban");
	}
}
