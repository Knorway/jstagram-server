package security.attempt1.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtEncoder jwtEncoder;

    public String generateAccessToken(Long userId) {
        Instant now = Instant.now();
        //        String scope = authentication.getAuthorities().stream()
        //            .map(GrantedAuthority::getAuthority)
        //            .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plus(3, ChronoUnit.DAYS))
            .subject(String.valueOf(userId))
            .claim("scope", "scope")
            .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    //    public String generateRefreshToken() {
    //        return "ok";
    //    }

}
