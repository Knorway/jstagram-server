package jstagram.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jstagram.server.domain.Comment;
import jstagram.server.domain.User;
import jstagram.server.dto.projection.CommentProjection;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonInclude(Include.NON_EMPTY)
public class CommentDto {

    private String content;

    private UserDto user;

    public void setComment(Comment comment) {
        this.content = comment.getContent();
    }

    public void setComment(CommentProjection commentProjection) {
        this.content = commentProjection.getContent();
    }

    public void setUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setUser(user);
        this.user = userDto;
    }

    public void setUser(CommentProjection commentProjection) {
        UserDto userDto = new UserDto();
        userDto.setUser(commentProjection);
        this.user = userDto;
    }
}
