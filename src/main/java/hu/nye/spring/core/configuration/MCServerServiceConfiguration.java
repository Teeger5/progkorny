package hu.nye.spring.core.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import hu.nye.spring.core.model.MCServer;
import lombok.extern.slf4j.Slf4j;

@Configuration
@PropertySource(value = "application.properties")
@Slf4j
public class MCServerServiceConfiguration {
/*
    @Value("${mcserver.name}")
    private String name;
    @Value("${mcserver.address}")
    private String address;
    @Value("${mcserver.description}")
    private String description;
    @Value("${mcserver.port}")
    private int port;
    @Value("${mcserver.max_players}")
    private int maxPlayers;

    @Bean
    public MCServer createLoggedUser(){
        log.info("Logged Minecraft server: {} {}:{}", name, address, port);
        return new MCServer(name, address, description, port, maxPlayers);
    }*/
}
