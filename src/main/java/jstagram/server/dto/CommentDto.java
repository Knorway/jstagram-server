package jstagram.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jstagram.server.domain.Comment;
import jstagram.server.dto.projection.CommentProjection;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CommentDto {

    private String content;

    @JsonInclude(Include.NON_NULL)
    private UserDto user;

    public void setComment(Comment comment) {
        this.content = comment.getContent();
    }

    public void setComment(CommentProjection commentProjection) {
        this.content = commentProjection.getContent();
    }

    public void setUser() {

    }

    public void setUser(CommentProjection commentProjection) {
        UserDto userDto = new UserDto();
        userDto.setUser(commentProjection);
        this.user = userDto;
    }
}
