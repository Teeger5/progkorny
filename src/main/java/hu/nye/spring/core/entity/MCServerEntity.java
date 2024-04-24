package hu.nye.spring.core.entity;

import hu.nye.spring.core.repistory.IMCVersionRepository;
import hu.nye.spring.core.request.MCServerRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "servers")
public class MCServerEntity {

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
		this.name = request.getName();
		this.address = request.getAddress();
		this.description = request.getDescription();
		this.port = request.getPort();
		this.maxPlayers = request.getMaxPlayers();
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
