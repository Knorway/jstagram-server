package jstagram.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jstagram.server.dto.projection.LikesProjection;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonInclude(Include.NON_EMPTY)
public class LikesDto {

    private Long id;
    private Long likes;
    private boolean isLiked;

    private UserDto user;
    private PostDto post;

    public void setLikes(LikesProjection projection) {
        this.id = projection.getLikesId();
        this.likes = projection.getLikes();
        this.isLiked = projection.getLiked() > 0;
    }

    public void setUser(LikesProjection projection) {
        UserDto userDto = new UserDto();
        userDto.setUser(projection);
        this.user = userDto;
    }

    public void setPost(LikesProjection projection) {
        PostDto postDto = new PostDto();
        postDto.setPost(projection);
        this.post = postDto;
    }
}
