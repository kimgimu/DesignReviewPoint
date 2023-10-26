package com.kimu.dichamsi.service;

import com.kimu.dichamsi.model.Posting;
import com.kimu.dichamsi.model.PostingDTO;
import com.kimu.dichamsi.model.PostingImages;
import com.kimu.dichamsi.repository.PostingImagesRepository;
import com.kimu.dichamsi.repository.PostingRepositoy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class PostingService {

    @Value("${spring.servlet.multipart.location}")
    private String fileUploadLocation;

    private final PostingRepositoy postingRepositoy;
    private final PostingImagesRepository postingImagesRepository;

    public PostingService(PostingRepositoy postingRepositoy, PostingImagesRepository postingImagesRepository) {
        this.postingRepositoy = postingRepositoy;
        this.postingImagesRepository = postingImagesRepository;
    }

    public void savePosting(PostingDTO postingDTO,
                             MultipartFile[] images) throws IOException {
        Posting postingEntity = postingRepositoy.save(postingDTO.toEntity());

        if (!Objects.isNull(images)) {
            for (MultipartFile image : images) {
                UUID uuid = UUID.randomUUID();
                String uploadFileName = image.getOriginalFilename();
                String fullPath = fileUploadLocation + uploadFileName;
                PostingImages postingImages = PostingImages.builder()
                        .uploadFileName(uploadFileName)
                        .storedFileName(uuid.toString())
                        .fullPath(fullPath)
                        .posting(postingEntity)
                        .build();
                PostingImages postingImagesEntity = postingImagesRepository.save(postingImages);
                image.transferTo(new File(fullPath));
            }
        }
    }

}
