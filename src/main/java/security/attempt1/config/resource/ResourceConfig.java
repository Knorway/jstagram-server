package security.attempt1.config.resource;

import static java.lang.String.format;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class ResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        var path = format("file:///%s/src/main/resources", System.getProperty("user.dir"));

        registry.addResourceHandler("/static-client/**")
            .addResourceLocations(path + "/client/static-client/");

        registry.addResourceHandler("/static-admin/**")
            .addResourceLocations(path + "/admin/static-admin/");
    }
}
