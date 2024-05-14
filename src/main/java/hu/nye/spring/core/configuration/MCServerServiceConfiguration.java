package hu.nye.spring.core.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Beállításokért felelős osztály
 * Az EnableWebMcv a CORS engedélyezéséhez kell,
 * ami a webes felület működéséhez kell
 */
@Configuration
//@PropertySource(value = "application.properties")
@EnableWebMvc
public class MCServerServiceConfiguration implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/*");
	}
}
