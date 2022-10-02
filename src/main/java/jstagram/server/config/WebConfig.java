package jstagram.server.config;

import static java.lang.String.format;

import java.util.List;
import java.util.concurrent.TimeUnit;
import jstagram.server.config.security.JwtTokenArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtTokenArgumentResolver jwtTokenArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(jwtTokenArgumentResolver);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String resourcePath = format(
            "file:///%s/src/main/resources",
            System.getProperty("user.dir")
        );
        String imagePath = format("file:///%s/upload/", System.getProperty("user.dir"));

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
