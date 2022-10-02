package jstagram.server.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadPostRequestDto {

    private String content;
    private MultipartFile image;
}
