package hu.nye.spring.core.entity;

import hu.nye.spring.core.model.dto.MCServerDTO;
import jakarta.persistence.*;
import lombok.*;

/**
 * Ez az adatbázis servers tábláját írja le
 * Az ID-t inkább nem küldjük ki
 * A törlés és a frissítés iknább a cím alapján történik majd
 */

@Entity
@Table(name = "servers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MCServerEntity {

	@Getter(AccessLevel.NONE)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, length = 120)
	private String name;
	@Column(nullable = false, unique = true, length = 120)
	private String address;
	@Column(length = 512)
	private String description;
	@ManyToOne
	@JoinColumn(name = "version", referencedColumnName = "id", nullable = false)
	private MCVersionEntity version;
	@Column(nullable = false)
	private Integer port;
	@Column(name = "max_players", nullable = false)
	private Integer maxPlayers;

	/**
	 * Frissíti ennek az objektumnak az értékeit
	 * a paraméterben megadottal.
	 * Minden értéket átállít
	 * @param data ez adja az új értékeket
	 */
	public void updateWith(MCServerDTO data) {
		if (data.getName() != null) {
			this.name = data.getName();
		}
		if (data.getDescription() != null) {
			this.description = data.getDescription();
		}
		if (data.getPort() != null) {
			this.port = data.getPort();
		}
		if (data.getMaxPlayers() != null) {
			this.maxPlayers = data.getMaxPlayers();
		}
	}

	public MCServerEntity(MCServerDTO data, MCVersionEntity version) {
		this(null,
				data.getName(),
				data.getAddress(),
				data.getDescription(),
				version,
				data.getPort(),
				data.getMaxPlayers()
		);
	}
}
