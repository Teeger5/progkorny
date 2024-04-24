package hu.nye.spring.core.repistory;

import hu.nye.spring.core.entity.MCServerEntity;
import hu.nye.spring.core.entity.MCVersionEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public class MCServerSpecification {

	public static Specification<MCServerEntity> hasVersion(List<MCVersionEntity> versions) {
		return (root, query, criteriaBuilder) -> {
			if (versions == null || versions.isEmpty()) {
				return criteriaBuilder.conjunction();
			}
			return root.get("version").in(versions);
		};
	}

	public static Specification<MCServerEntity> maxPlayersLessThanEqual(Optional<Integer> maxPlayers) {
		return (root, query, criteriaBuilder) -> {
			if (maxPlayers == null || maxPlayers.isEmpty()) {
				return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
			}
			return criteriaBuilder.lessThanOrEqualTo(root.get("maxPlayers"), maxPlayers.get());
		};
	}

	public static Specification<MCServerEntity> nameContains(Optional<String> name) {
		return (root, query, criteriaBuilder) -> {
			if (name == null || name.isEmpty() || name.get().isEmpty()) {
				return criteriaBuilder.conjunction();
			}
			return criteriaBuilder.like(root.get("name"), "%" + name.get() + "%");
		};
	}
}
