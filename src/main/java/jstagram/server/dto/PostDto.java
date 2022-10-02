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

    public void setPost(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.imgUrl = post.getImgUrl();
        this.createdAt = post.getCreatedAt();
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
        List<CommentProjection> targetComments = comments.get(this.id);
        if (targetComments != null) {
            this.comments = targetComments.stream()
                .map(comment -> {
                    CommentDto commentDto = new CommentDto();
                    commentDto.setComment(comment);
                    commentDto.setUser(comment);
                    return commentDto;
                })
                .toList();
        }
    }

    //    public PostDto(Post post) {
    //        this.id = post.getId();
    //        this.content = post.getContent();
    //        this.imgUrl = post.getImgUrl();
    //    }
    //
    //    public PostDto(Post post, User user) {
    //        this(post);
    //        this.user = new MemberDto(user);
    //    }

    //    public PostDto(Post post, User user, List<Comment> comments) {
    //        this(post, user);
    //        this.comments = comments.stream().map(c -> new CommentDto(c.getContent())).toList();
    //    }
}
