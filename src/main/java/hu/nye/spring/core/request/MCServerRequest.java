package hu.nye.spring.core.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MCServerRequest {

    private String name;
    private String address;
    private String description;
    private String version;
    private int port;
    private int maxPlayers;
}
