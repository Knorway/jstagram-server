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

    @GetMapping("/")
    public String home() {
        return envProperties.clientUrl();
    }

    //    @GetMapping("/")
    //    public String home() {
    //        return """
    //            <a href='/oauth2/authorization/google'>google login</a>
    //            <a href='/oauth2/authorization/naver'>naver login</a>
    //            """;
    //    }
    //
    //    @GetMapping("/test")
    //    public String test() throws IOException, InterruptedException {
    //        var client = HttpClient.newHttpClient();
    //        var request = HttpRequest.newBuilder()
    //            .uri(URI.create("https://jsonplaceholder.typicode.com/todos"))
    //            .build();
    //        var response = client.send(request, BodyHandlers.ofString());
    //        return response.body();
    //    }
}
