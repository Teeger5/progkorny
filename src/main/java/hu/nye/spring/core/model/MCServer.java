package hu.nye.spring.core.model;

import hu.nye.spring.core.entity.MCServerEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class MCServer {

    private String name;
    private String address;
    private String version;
    private String description;
    private int port;
    private int maxPlayers;

    public static MCServer fromEntity(MCServerEntity entity) {
        return new MCServer(
                entity.getName(),
                entity.getAddress(),
                entity.getVersion().getName(),
                entity.getDescription(),
                entity.getPort(),
                entity.getMaxPlayers());
    }
}
