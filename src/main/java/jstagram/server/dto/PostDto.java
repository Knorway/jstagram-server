package jstagram.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jstagram.server.domain.Comment;
import jstagram.server.domain.Post;
import jstagram.server.domain.User;
import jstagram.server.dto.projection.CommentProjection;
import jstagram.server.dto.projection.LikesProjection;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonInclude(Include.NON_EMPTY)
public class PostDto {

    private Long id;
    private String content;
    private String imgUrl;
    private LocalDateTime createdAt;

    private UserDto user;
    private List<CommentDto> comments = new ArrayList<>();
    private LikesDto likes;

    public void setPost(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.imgUrl = post.getImgUrl();
        this.createdAt = post.getCreatedAt();
    }

    public void setPost(LikesProjection projection) {
        this.id = projection.getPostId();
    }

    public void setUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setUser(user);
        this.user = userDto;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments.stream().map(comment -> {
            CommentDto commentDto = new CommentDto();
            commentDto.setComment(comment);
            return commentDto;
        }).toList();
    }

    public void setComments(Map<Long, List<CommentProjection>> comments) {
        List<CommentProjection> projections = comments.get(this.id);
        if (projections != null) {
            this.comments = projections.stream()
                .map(projection -> {
                    CommentDto commentDto = new CommentDto();
                    commentDto.setComment(projection);
                    commentDto.setUser(projection);
                    return commentDto;
                })
                .toList();
        }
    }

    public void setLikes(Map<Long, LikesProjection> likes) {
        LikesProjection projection = likes.get(this.id);
        if (projection != null) {
            LikesDto likesDto = new LikesDto();
            likesDto.setLikes(projection);
            likesDto.setPost(projection);
            likesDto.setUser(projection);
            this.likes = likesDto;
        }
    }
}
