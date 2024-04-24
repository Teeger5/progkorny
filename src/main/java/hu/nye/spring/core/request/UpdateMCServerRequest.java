package hu.nye.spring.core.request;

import lombok.*;

import java.util.Optional;

/**
 * Ez azt a kérést írja le,
 * amit egy szerver adatainak frissítésekor kell küldeni
 * Frissítéskor kötelezően csak az address-t kell megadni,
 * mert ez azonosít egy szervert egyértelműen az ID-n kívül
 * Ezen persze lehet, később érdemes lenne változtatni,
 * és az ID-t is küldeni a kliensnek, viszont ezt nem tudom,
 * jó ötlet-e elküldeni a kliensnek
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdateMCServerRequest {

    private Optional<String> name;
    private String address;
    private Optional<String> description;
    private Optional<String> version;
    private Optional<Integer> port;
    private Optional<Integer> maxPlayers;
}
