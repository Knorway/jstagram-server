package security.attempt1;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import security.attempt1.config.properties.EnvProperties;
import security.attempt1.config.properties.RsaKeyProperties;

@Configuration
@EnableConfigurationProperties({RsaKeyProperties.class, EnvProperties.class})
public class ApplicationConfig {

}
