package hu.nye.spring.core.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import hu.nye.spring.core.model.MCServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Beállításokért felelős osztály
 * Az EnableWebMcv a CORS engedélyezéséhez kell,
 * ami a webes felület működéséhez kell
 */
@Configuration
@PropertySource(value = "application.properties")
@EnableWebMvc
public class MCServerServiceConfiguration implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/*");
	}
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
