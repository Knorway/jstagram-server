package security.attempt1.controller;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import security.attempt1.config.properties.RsaKeyProperties;
import security.attempt1.domain.User;
import security.attempt1.dto.LoginRequest;
import security.attempt1.repository.UserRepository;
import security.attempt1.service.AuthService;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final PasswordEncoder encoder;
    private final RsaKeyProperties rsaKeyProperties;

    @PostMapping("/register")
    public String register(@RequestBody LoginRequest body) {
        var exUser = userRepository.findByUsername(body.getUsername());
        if (exUser != null) {
            throw new RuntimeException("user already exists");
        }

        var user = new User();
        user.setUsername(body.getUsername());
        user.setPassword(encoder.encode(body.getPassword()));
        userRepository.save(user);

        return authService.generateAccessToken(user.getId());
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest body) {
        var user = userRepository.findByUsername(body.getUsername());
        if (!encoder.matches(body.getPassword(), user.getPassword())) {
            throw new RuntimeException("invalid password");
        }
        return authService.generateAccessToken(user.getId());
    }

    @GetMapping("/validate")
    public User test(Authentication authentication, Principal principal) {
        var user = userRepository.findById(Long.parseLong(principal.getName()));
        if (user.isEmpty()) {
            throw new RuntimeException("no user");
        }
        //        System.out.println(principal.getName());
        //        System.out.println(authentication.getAuthorities());
        //        System.out.println(authentication.getCredentials());
        //        System.out.println(authentication.getDetails());
        //        System.out.println(authentication.getPrincipal());
        return user.get();
    }
}
