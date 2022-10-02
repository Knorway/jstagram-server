package jstagram.server.dto.request;

import lombok.Data;

@Data
public class PostCommentRequestDto {

    private String content;
    private Long postId;
}
