package security.attempt1.config.security;

import static java.lang.String.format;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import security.attempt1.config.properties.EnvProperties;
import security.attempt1.domain.User;
import security.attempt1.repository.UserRepository;
import security.attempt1.service.AuthService;

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
    ) throws IOException, ServletException {
        var provider = String.valueOf((request.getRequestURL())).split("code/")[1];
        var principal = (OAuth2User) authentication.getPrincipal();

        var id = extractId(provider, principal);
        var email = extractEmail(provider, principal);

        var clientUrl = env.clientUrl();
        var token = generateRedirectToken(id, email, provider);

        response.sendRedirect(format("%s?redirect=%s", clientUrl, token));
    }

    private String extractId(String provider, OAuth2User principal) {
        var id = (String) switch (provider) {
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
        var email = (String) switch (provider) {
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
        var exUser = userRepository.findByUsername(email);
        if (exUser != null) {
            return authService.generateAccessToken(exUser.getId());
        }

        var user = new User();
        user.setProvider(format("%s-%s", provider, id));
        user.setUsername(email);
        userRepository.save(user);

        return authService.generateAccessToken(user.getId());
    }
}
