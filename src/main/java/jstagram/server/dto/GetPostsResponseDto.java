package jstagram.server.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class GetPostsResponseDto {

    private Long userId;
    private Long postId;
    private String imgSrc;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public GetPostsResponseDto(
        Long userId,
        Long postId,
        String imgSrc,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        this.userId = userId;
        this.postId = postId;
        this.imgSrc = imgSrc;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
