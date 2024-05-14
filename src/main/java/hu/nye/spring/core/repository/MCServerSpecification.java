package hu.nye.spring.core.repository;

import hu.nye.spring.core.entity.MCServerEntity;
import hu.nye.spring.core.entity.MCVersionEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Ez az osztály a WHERE feltételek
 * írja le Minecraft szerverek kiválasztásához
 */
public class MCServerSpecification {

	/**
	 * Csak ezek a verziók lesznek kiválasztva
	 * @param versions
	 * @return
	 */
	public static Specification<MCServerEntity> hasVersion(List<MCVersionEntity> versions) {
		return (root, query, criteriaBuilder) -> {
			if (versions == null || versions.isEmpty()) {
				return criteriaBuilder.conjunction();
			}
			return root.get("version").in(versions);
		};
	}

	/**
	 * A maxPlayers értéke legfeljebb ennyi legyen
	 * @param maxPlayers
	 * @return
	 */
	public static Specification<MCServerEntity> maxPlayersLessThanEqual(Integer maxPlayers) {
		return (root, query, criteriaBuilder) -> {
			if (maxPlayers == null) {
				return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
			}
			return criteriaBuilder.lessThanOrEqualTo(root.get("maxPlayers"), maxPlayers);
		};
	}

	/**
	 * A szerver neve tartalmazza a megadott szöveget
	 * @param name
	 * @return
	 */
	public static Specification<MCServerEntity> nameContains(String name) {
		return (root, query, criteriaBuilder) -> {
			if (name == null || name.isEmpty()) {
				return criteriaBuilder.conjunction();
			}
			return criteriaBuilder.like(root.get("name"), "%" + name + "%");
		};
	}
}
