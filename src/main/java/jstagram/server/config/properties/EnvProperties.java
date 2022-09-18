package jstagram.server.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "env")
public record EnvProperties(String clientUrl, String secret) {

}
