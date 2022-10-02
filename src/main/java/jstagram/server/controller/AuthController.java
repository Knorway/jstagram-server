package jstagram.server.controller;

import java.util.Optional;
import jstagram.server.config.properties.RsaKeyProperties;
import jstagram.server.config.security.JwtToken;
import jstagram.server.config.security.TokenUser;
import jstagram.server.domain.User;
import jstagram.server.dto.UserDto;
import jstagram.server.dto.request.LoginRequestDto;
import jstagram.server.repository.UserRepository;
import jstagram.server.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final PasswordEncoder encoder;
    private final RsaKeyProperties rsaKeyProperties;

    @PostMapping("/register")
    public String register(@RequestBody LoginRequestDto requestDto) {
        User exUser = userRepository.findByUsername(requestDto.getUsername());
        if (exUser != null) {
            throw new RuntimeException("user already exists");
        }

        User user = new User();
        user.setUsername(requestDto.getUsername());
        user.setPassword(encoder.encode(requestDto.getPassword()));
        userRepository.save(user);

        return authService.generateAccessToken(user.getId());
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDto requestDto) {
        User user = userRepository.findByUsername(requestDto.getUsername());
        if (!encoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("invalid password");
        }
        return authService.generateAccessToken(user.getId());
    }

    @PostMapping("/validate")
    public UserDto validate(@JwtToken TokenUser auth) {
        Optional<User> user = userRepository.findById(auth.getId());
        if (user.isEmpty()) {
            throw new RuntimeException("no user");
        }

        UserDto userDto = new UserDto();
        userDto.setUser(user.get());

        return userDto;
    }

    @PostMapping("/refresh")
    public String refreshToken() {
        return "todo";
    }
}
