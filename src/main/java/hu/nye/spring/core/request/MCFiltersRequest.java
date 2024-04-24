package hu.nye.spring.core.request;

import lombok.*;

import java.util.List;
import java.util.Optional;

/**
 * Ez az osztály azt a kérést írja le,
 * amiben a szerverek lekérdezéskor küld a kliens
 * Ezeket a szűrési lehetőségeket használhatja,
 * amelyeket opcionálisan adhat meg,
 * azaz a küldött JSON-nek nem feltétlenül kell tartalmaznia bármelyiket
 * Ezért vannak Optional formában
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString

public class MCFiltersRequest {
	/**
	 * Kiválasztott verziók
	 */
	private List<String> versions;
	/**
	 * Maximális játékosszám legalább
	 */
	private Integer maxPlayersMin;
	/**
	 * Maximális játékosszám legfeljebb
	 */
	private Integer maxPlayersMax;
	/**
	 * A szerver nevének tartalmaznia kell
	 */
	private String partOfName;
}
