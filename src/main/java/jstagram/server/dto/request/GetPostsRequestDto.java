package jstagram.server.dto.request;

import lombok.Data;

@Data
public class GetPostsRequestDto {

    private int page;
    private int limit;
}
