package jstagram.server.service;

import static java.lang.String.format;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import jstagram.server.domain.Comment;
import jstagram.server.domain.Fellowship;
import jstagram.server.domain.Likes;
import jstagram.server.domain.Post;
import jstagram.server.domain.User;
import jstagram.server.dto.request.GetPostsRequestDto;
import jstagram.server.dto.request.PostCommentRequestDto;
import jstagram.server.dto.request.UploadPostRequestDto;
import jstagram.server.repository.CommentRepository;
import jstagram.server.repository.FellowshipRepository;
import jstagram.server.repository.LikesRepository;
import jstagram.server.repository.PostRepository;
import jstagram.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class PostService {

    private final EntityManager em;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FellowshipRepository fellowshipRepository;
    private final CommentRepository commentRepository;
    private final LikesRepository likesRepository;

    private final String dir = System.getProperty("user.dir") + "/upload/";

    public List<Post> getMainPagePosts(Long userId, GetPostsRequestDto requestDto) {
        List<Fellowship> followings = fellowshipRepository.findFollowingsByUserId(userId);
        List<Long> followingIds = followings.stream()
            .map(e -> e.getUser().getId())
            .collect(Collectors.toList());

        int page = requestDto.getPage();
        int limit = requestDto.getLimit();
        PageRequest pageParam = PageRequest.of(page, limit, Sort.by(Direction.DESC, "id"));

        return postRepository.findRelatedPosts(userId, followingIds, pageParam);
    }

    public Post savePost(Long userId, UploadPostRequestDto request) {
        User userReference = em.getReference(User.class, userId);
        String imgSrc = savePostImageFile(request.getImage());

        Post post = new Post();
        post.setUser(userReference);
        post.setImgUrl(imgSrc);
        post.setContent(request.getContent());

        return postRepository.save(post);
    }


    public Comment saveComment(Long userId, PostCommentRequestDto requestDto) {
        Comment comment = new Comment();
        comment.setUser(em.getReference(User.class, userId));
        comment.setPost(em.getReference(Post.class, requestDto.getPostId()));
        comment.setContent(requestDto.getContent());
        return commentRepository.save(comment);
    }

    public boolean likeOrUnlikePost(Long userId, Long postId) {
        Likes alreadyLiked = likesRepository.findByPostIdAndUserId(postId, userId);
        if (alreadyLiked == null) {
            Likes likes = new Likes();
            likes.setUser(em.getReference(User.class, userId));
            likes.setPost(em.getReference(Post.class, postId));
            likesRepository.save(likes);
            return true;
        }

        likesRepository.delete(alreadyLiked);
        return false;
    }

    private String savePostImageFile(MultipartFile image) {
        UUID uuid = UUID.randomUUID();
        String fileName = format("%s_%s", uuid, image.getOriginalFilename());
        String fileDir = dir + fileName;

        try {
            image.transferTo(new File(fileDir));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return fileDir;
    }
}
