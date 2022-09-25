package jstagram.server.service;

import static java.lang.String.format;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import jstagram.server.domain.Post;
import jstagram.server.domain.User;
import jstagram.server.dto.UploadPostRequest;
import jstagram.server.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class PostService {

    private final EntityManager em;
    private final PostRepository postRepository;

    private final String dir = System.getProperty("user.dir") + "/upload/";

    public List<Post> getPosts(Long userId) {
        return postRepository.findByUserId(userId);
    }

    public Post savePost(Long userId, UploadPostRequest request) {
        User userReference = em.getReference(User.class, userId);
        String imgSrc = savePostImageFile(request.getImage());

        Post post = new Post();
        post.setUser(userReference);
        post.setImgUrl(imgSrc);
        post.setContent(request.getContent());

        return postRepository.save(post);
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
