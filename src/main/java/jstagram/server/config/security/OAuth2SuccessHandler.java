package jstagram.server.config.security;

import static java.lang.String.format;

import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jstagram.server.config.properties.EnvProperties;
import jstagram.server.domain.User;
import jstagram.server.repository.UserRepository;
import jstagram.server.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final AuthService authService;

    private final EnvProperties env;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException {
        String provider = String.valueOf((request.getRequestURL())).split("code/")[1];
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();

        String id = extractId(provider, principal);
        String email = extractEmail(provider, principal);

        String clientUrl = env.clientUrl();
        String token = generateRedirectToken(id, email, provider);

        response.sendRedirect(format("%s?redirect=%s", clientUrl, token));
    }

    private String extractId(String provider, OAuth2User principal) {
        String id = (String) switch (provider) {
            case "google" -> principal.getAttribute("sub");
            case "naver" -> {
                Map<String, Object> res = principal.getAttribute("response");
                yield res != null ? res.get("id") : "";
            }
            default -> "";
        };
        if ("".equals(id)) {
            throw new RuntimeException(format("error extracting id from %s principal", provider));
        }
        return id;
    }

    private String extractEmail(String provider, OAuth2User principal) {
        String email = (String) switch (provider) {
            case "google" -> principal.getAttribute("email");
            case "naver" -> {
                Map<String, Object> res = principal.getAttribute("response");
                yield res != null ? res.get("email") : "";
            }
            default -> "";
        };
        if ("".equals(email)) {
            throw new RuntimeException(format(
                "error extracting email from %s principal",
                provider
            ));
        }
        return email;
    }

    private String generateRedirectToken(String id, String email, String provider) {
        User exUser = userRepository.findByUsername(email);
        if (exUser != null) {
            return authService.generateAccessToken(exUser.getId());
        }

        User user = new User();
        user.setProvider(format("%s-%s", provider, id));
        user.setUsername(email);
        userRepository.save(user);

        return authService.generateAccessToken(user.getId());
    }
}
