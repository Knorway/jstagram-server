package jstagram.server.controller;

import java.security.Principal;
import java.util.Optional;
import jstagram.server.config.properties.RsaKeyProperties;
import jstagram.server.domain.User;
import jstagram.server.dto.LoginRequest;
import jstagram.server.repository.UserRepository;
import jstagram.server.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        User exUser = userRepository.findByUsername(body.getUsername());
        if (exUser != null) {
            throw new RuntimeException("user already exists");
        }

        User user = new User();
        user.setUsername(body.getUsername());
        user.setPassword(encoder.encode(body.getPassword()));
        userRepository.save(user);

        return authService.generateAccessToken(user.getId());
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest body) {
        User user = userRepository.findByUsername(body.getUsername());
        if (!encoder.matches(body.getPassword(), user.getPassword())) {
            throw new RuntimeException("invalid password");
        }
        return authService.generateAccessToken(user.getId());
    }

    @PostMapping("/validate")
    public User validate(Authentication authentication, Principal principal) {
        //        System.out.println("principal.getName()" + principal.getName());
        //        System.out.println("authentication.getAuthorities()" + authentication.getAuthorities());
        //        System.out.println("authentication.getCredentials()" + authentication.getCredentials());
        //        System.out.println("authentication.getDetails()" + authentication.getDetails());
        //        System.out.println("authentication.getPrincipal()" + authentication.getPrincipal());

        Optional<User> user = userRepository.findById(Long.parseLong(principal.getName()));
        if (user.isEmpty()) {
            throw new RuntimeException("no user");
        }

        return user.get();
    }

    @PostMapping("/refresh")
    public String refreshToken() {
        return "todo";
    }
}
