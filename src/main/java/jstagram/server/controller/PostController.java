package jstagram.server.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import jstagram.server.config.security.JwtToken;
import jstagram.server.config.security.TokenUser;
import jstagram.server.domain.Comment;
import jstagram.server.domain.Post;
import jstagram.server.dto.PostDto;
import jstagram.server.dto.projection.CommentProjection;
import jstagram.server.dto.request.GetPostsRequestDto;
import jstagram.server.dto.request.PostCommentRequestDto;
import jstagram.server.dto.request.UploadPostRequestDto;
import jstagram.server.repository.PostRepository;
import jstagram.server.repository.UserRepository;
import jstagram.server.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class PostController {

    private final EntityManager em;
    private final PostService postService;

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @GetMapping("/post")
    public List<PostDto> getPosts(
        @JwtToken TokenUser user,
        @ModelAttribute GetPostsRequestDto requestDto
    ) {
        List<Post> posts = postService.getMainPagePosts(user.getId(), requestDto);
        List<Long> postIds = posts.stream().map(Post::getId).toList();

        Map<Long, List<CommentProjection>> commentsMap = postRepository.getLimitedComments(
                3,
                postIds
            )
            .stream()
            .collect(Collectors.groupingBy(CommentProjection::getPostId));

        for (List<CommentProjection> value : commentsMap.values()) {
            for (CommentProjection commentProjection : value) {
                System.out.println(commentProjection.getContent());
            }
        }

        return posts.stream().map(post -> {
            PostDto dto = new PostDto();
            dto.setPost(post);
            dto.setUser(post.getUser());
            dto.setComments(commentsMap);
            return dto;
        }).toList();

    }

    @GetMapping("/post/{postId}")
    public String getPost() {
        return "post";
    }

    @PostMapping("/post")
    public HashMap<String, Object> uploadPost(
        @ModelAttribute UploadPostRequestDto requestDto,
        @JwtToken TokenUser user
    ) {
        MultipartFile image = requestDto.getImage();
        if (image.isEmpty()) {
            throw new RuntimeException("no image provided");
        }

        Post post = postService.savePost(user.getId(), requestDto);

        HashMap<String, Object> map = new HashMap<>();
        map.put("postId", post.getId());
        return map;
    }

    @PostMapping("/post/comment")
    public String createComment(
        @RequestBody PostCommentRequestDto requestDto,
        @JwtToken TokenUser user
    ) {
        Comment comment = postService.saveComment(user.getId(), requestDto);
        return comment.getContent();
    }

    @PostMapping("/post/like")
    public String createLike() {
        return "ok";
    }
}
