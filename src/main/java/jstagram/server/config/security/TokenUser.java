package jstagram.server.config.security;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TokenUser {

    private final Long id;

    public TokenUser(String id) {
        this.id = Long.valueOf(id);
    }
}
