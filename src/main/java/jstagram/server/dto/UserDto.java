package jstagram.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jstagram.server.domain.User;
import jstagram.server.dto.projection.CommentProjection;
import jstagram.server.dto.projection.LikesProjection;
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
        this.provider = user.getProvider();
    }

    public void setUser(CommentProjection projection) {
        this.id = projection.getUserId();
        this.username = projection.getUsername();
    }

    public void setUser(LikesProjection projection) {
        this.id = projection.getUserId();
    }
}
