package jstagram.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jstagram.server.domain.User;
import jstagram.server.dto.projection.CommentProjection;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonInclude(Include.NON_EMPTY)
public class UserDto {

    private Long id;
    private String username;
    private String provider;

    public void setUser(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }

    public void setUser(CommentProjection commentProjection) {
        this.id = commentProjection.getUserId();
        this.username = commentProjection.getUsername();
    }
}
