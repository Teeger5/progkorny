package hu.nye.spring.core.entity;

import hu.nye.spring.core.request.MCServerRequest;
import jakarta.persistence.*;
import lombok.*;

/**
 * Ez az adatbázis servers tábláját írja le
 * Az ID-t inkább nem küldjük ki
 * A törlés és a frissítés iknább a cím alapján történik majd
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "servers")
public class MCServerEntity {

	@Getter(AccessLevel.NONE)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
/*			strategy = GenerationType.AUTO,
			generator = "native")
	@GenericGenerator(
			name = "native",
			strategy = "native")*/
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
	 * @param request ez adja az új értékeket
	 */
	public void updateWith(MCServerRequest request) {
		if (request.getName() != null) {
			this.name = request.getName();
		}
		if (request.getDescription() != null) {
			this.description = request.getDescription();
		}
		if (request.getPort() != null) {
			this.port = request.getPort();
		}
		if (request.getMaxPlayers() != null) {
			this.maxPlayers = request.getMaxPlayers();
		}
	}

	public MCServerEntity(MCServerRequest request, MCVersionEntity version) {
		request.normalize();
		name = request.getName().strip();
		address = request.getAddress()
				.strip().toLowerCase();
		description = request.getDescription().strip();
		this.version = version;
		port = request.getPort();
		maxPlayers = request.getMaxPlayers();
	}

	/**
	 * Új objektumot hoz létre egy Requestben lévő adatokat felhasználva
	 * @param request a forrás request
	 * @return
	 */
	public static MCServerEntity fromRequest (MCServerRequest request, MCVersionEntity version) {
		return new MCServerEntity(
				null,
				request.getName(),
				request.getAddress(),
				request.getDescription(),
				version,
				request.getPort(),
				request.getMaxPlayers());
	}
}
