package hu.nye.spring.core.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Ez az osztály a versions táblát írja le
 * Ez idegenkulcsként kapcsolódik a servers táblához
 */
@Entity
@Table(name = "versions")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MCVersionEntity {

	@Id
	private Integer id;

	@Getter
	@Column(nullable = false, unique = true, length = 32)
	private String name;

//	@OneToMany(mappedBy = "version")
//	private Set<MCServerEntity> servers;

	/**
	 * Ez megváltozataja a JSON-reprezentációját az objektumnak
	 * Azaz, ha ez a metódus egy String-et ad vissza,
	 * akkor a JSON-ben is egy String jelenik meg,
	 * nem a teljes objektum átalakítva
	 * @return a verzió neve, mert csak ennek kell megjelennie
	 */
	@JsonValue
	public String getName() {
		return name;
	}

	/**
	 * Teszteléskor hasznos
	 * @param name verzió neve
	 */
	public MCVersionEntity(String name) {
		this.name = name;
	}
}
