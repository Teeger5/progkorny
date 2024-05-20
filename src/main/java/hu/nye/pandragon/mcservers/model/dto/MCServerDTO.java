package hu.nye.pandragon.mcservers.model.dto;

import hu.nye.pandragon.mcservers.entity.MCServerEntity;
import hu.nye.pandragon.mcservers.request.MCServerRequest;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Ez az osztály egy Minecraft-szervert ír le
 * A konstruktorban elvégez néhány módosítást az adatokon:
 * - .strip() metódus a szöveges adatokra
 * - .toLowerCase() a címre
 */
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class MCServerDTO {

	private String name;
	private String address;
	private String version;
	private String description;
	private Integer port;
	private Integer maxPlayers;

	/**
	 * Létrehoz egy új példányt egy MCServerEntity alapján
	 * @param entity
	 */
	public MCServerDTO(MCServerEntity entity) {
		this(
				entity.getName(),
				entity.getAddress(),
				entity.getVersion().getName(),
				entity.getDescription(),
				entity.getPort(),
				entity.getMaxPlayers());
	}

	public MCServerDTO(MCServerRequest request) {
		this(
				request.getName().strip(),
				request.getAddress()
						.strip().toLowerCase(),
				request.getVersion().strip(),
				request.getDescription().strip(),
				request.getPort(),
				request.getMaxPlayers());
	}
}
