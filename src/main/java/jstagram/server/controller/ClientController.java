package jstagram.server.controller;

import static java.lang.String.format;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.annotation.PostConstruct;
import jstagram.server.config.properties.EnvProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClientController {

    private final EnvProperties envProperties;

    private byte[] clientDist;
    private byte[] adminDist;

    // TODO: static resource custom research
    // TODO: dynamic application.properties and config
    // TODO: about caching static assets on both browser and server

    /**
     * rewrites to serve client in production
     * @return built react index.html file
     */
    @GetMapping(path = "*", produces = MediaType.TEXT_HTML_VALUE)
    public byte[] serveClient() {
        return this.clientDist;
    }

    /**
     * rewrites to serve admin client in production
     * @return built react index.html file
     */
    @GetMapping(path = {"/admin", "/admin/**"}, produces = MediaType.TEXT_HTML_VALUE)
    public byte[] serveAdminClient() {
        return this.adminDist;
    }

    /**
     * loads built client files on memory for fast serving
     */
    @PostConstruct
    public void loadDist() throws IOException {
        var path = format("%s/src/main/resources", System.getProperty("user.dir"));
        this.clientDist = Files.readAllBytes(Paths.get(format("%s/client/index.html", path)));
        this.adminDist = Files.readAllBytes(Paths.get(format("%s/admin/index.html", path)));
    }
}
