package security.attempt1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import security.attempt1.config.properties.EnvProperties;

@RestController
@RequiredArgsConstructor
public class ClientController {

    private final EnvProperties envProperties;

    // TODO: static resource custom research
    // TODO: dynamic application.properties and config

    /**
     * rewrites to serve client in production
     * @return built react index.html file
     */
    @GetMapping(path = "*", produces = MediaType.TEXT_HTML_VALUE)
    public String serveClient() {
        return "react client";
    }

    /**
     * rewrites to serve admin client in production
     * @return built react index.html file
     */
    @GetMapping(path = {"/admin", "/admin/**"}, produces = MediaType.TEXT_HTML_VALUE)
    public String serveAdminClient() {
        return "admin react client";
    }
}
