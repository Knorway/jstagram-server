package jstagram.server.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadPostRequest {

    private String content;
    private MultipartFile image;
}
