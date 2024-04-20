package hu.nye.spring.core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "versions")
@AllArgsConstructor
@NoArgsConstructor
public class MCVersionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Getter
	@Column(nullable = false, unique = true, length = 32)
	private String name;

	@OneToMany(mappedBy = "version")
	private Set<MCServerEntity> servers;
/*
	public static MCVersionEntity from(String name) {
		return new MCVersionEntity(null, name, null)
	}*/

	/**
	 * Visszaadja a nevet, amiben kicseréli a '.'-okat '_'-ra
	 * @return az átalakított név
	 */
	public String getVersionID() {
		return name.replace('.', '_');
	}
}
