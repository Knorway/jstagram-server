package jstagram.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;
import java.util.List;
import jstagram.server.domain.User;
import lombok.Data;

@Data
public class MemberDto {

    private Long id;
    private String username;
    private String provider;

    @JsonInclude(Include.NON_EMPTY)
    private List<PostDto> posts = new ArrayList<>();

    public MemberDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.provider = user.getProvider();
    }

    public MemberDto(User user, PostDto postDto) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.provider = user.getProvider();
    }
}
