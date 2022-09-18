package jstagram.server;

import jstagram.server.config.properties.EnvProperties;
import jstagram.server.config.properties.RsaKeyProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({RsaKeyProperties.class, EnvProperties.class})
public class ApplicationConfig {

}
