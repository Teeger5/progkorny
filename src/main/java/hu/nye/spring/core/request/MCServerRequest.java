package hu.nye.spring.core.request;

import jakarta.validation.constraints.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MCServerRequest {

	@NotBlank(message = "A név nem lehet üres")
	private String name;

	@NotBlank(message = "A cím nem lehet üres")
	@Pattern(
			regexp = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$|" +
					"^(?!-)[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
			message = "Nem érvényes IP-cím vagy domain név")
	private String address;

	private String description;

	@NotBlank(message = "A verziót meg kell adni")
	private String version;

	@Min(value = 1, message = "Érvénytelen port")
	@Max(value = 65535, message = "Érvénytelen port")
	private Integer port;

	@Positive(message = "Legalább 1 játékost tudnia kell fogadni a szervernek")
	private Integer maxPlayers;

	/**
	 * Elvégez néhány módosítást a beérkezett adatokon:
	 * - .trim() metódus a szöveges adatokra
	 * - .toLowerCase() a címre
	 * A számadatokat az annotációk ellenőrzik,
	 * A szövegeket speciálisabban is kell
	 */
	public void normalize() {
		name = name.strip();
		description = description.strip();
		address = address.strip().toLowerCase();
	}
}
