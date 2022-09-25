package jstagram.server.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import jstagram.server.domain.Post;
import jstagram.server.dto.GetPostsResponseDto;
import jstagram.server.dto.UploadPostRequest;
import jstagram.server.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/api/post")
@RestController
public class PostController {

    private final PostService postService;

    @GetMapping
    public List<GetPostsResponseDto> getPosts(Principal principal) {
        List<Post> posts = postService.getPosts(Long.valueOf(principal.getName()));
        return posts.stream()
            .map(post -> new GetPostsResponseDto(
                post.getUser().getId(),
                post.getId(),
                post.getImgUrl(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt()
            )).collect(Collectors.toList());
    }

    @GetMapping("/{postId}")
    public String getPost() {
        return "post";
    }

    @PostMapping
    public HashMap<String, Object> uploadPost(
        @ModelAttribute UploadPostRequest body,
        Principal principal
    ) {
        MultipartFile image = body.getImage();
        if (image.isEmpty()) {
            throw new RuntimeException("no image provided");
        }

        Post post = postService.savePost(Long.valueOf(principal.getName()), body);

        HashMap<String, Object> map = new HashMap<>();
        map.put("postId", post.getId());
        return map;
    }
}
