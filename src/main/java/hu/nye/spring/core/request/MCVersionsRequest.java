package hu.nye.spring.core.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MCVersionsRequest {

    private String name;
    private String address;
    private String description;
    private String version;
    private int port;
    private int maxPlayers;
}
