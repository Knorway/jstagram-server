package jstagram.server.config.resource;

import static java.lang.String.format;

import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String resourcePath = format(
            "file:///%s/src/main/resources",
            System.getProperty("user.dir")
        );
        String imagePath = "file:///" + System.getProperty("user.dir") + "/upload/";

        registry.addResourceHandler("/static-client/**")
            .addResourceLocations(resourcePath + "/client/")
            .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));

        registry.addResourceHandler("/static-admin/**")
            .addResourceLocations(resourcePath + "/admin/");

        registry.addResourceHandler("/image/**")
            .addResourceLocations(imagePath)
            .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));
    }
}
