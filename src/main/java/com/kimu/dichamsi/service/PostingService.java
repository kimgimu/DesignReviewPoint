package com.kimu.dichamsi.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class PostingService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${spring.servlet.multipart.location}")
    private String fileUploadLocation;
    private final PostingRepositoy postingRepositoy;
    private final PostingImagesRepository postingImagesRepository;

    public PostingService(AmazonS3 amazonS3, PostingRepositoy postingRepositoy, PostingImagesRepository postingImagesRepository) {
        this.amazonS3 = amazonS3;
        this.postingRepositoy = postingRepositoy;
        this.postingImagesRepository = postingImagesRepository;
    }

    public void savePosting(PostingDTO postingDTO,
                             MultipartFile[] images) throws IOException {
        Posting postingEntity = postingRepositoy.save(postingDTO.toEntity());

        if (!Objects.isNull(images)) {
            for (MultipartFile image : images) {

                String originalFilename = image.getOriginalFilename();
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(image.getSize());
                metadata.setContentType(image.getContentType());

                amazonS3.putObject(bucket,originalFilename,image.getInputStream(),metadata);

                PostingImages postingImages = PostingImages.builder()
                        .images(amazonS3.getUrl(bucket, originalFilename).toString())
                        .posting(postingEntity)
                        .build();

                PostingImages postingImagesEntity = postingImagesRepository.save(postingImages);
            }
        }
    }

    public List<PostingDTO> showAllPosting(){
        List<Posting> postings = postingRepositoy.findAllWithImages();
        List<PostingDTO> postingDTOS = new ArrayList<>();
        for(Posting posting : postings){
            postingDTOS.add(posting.toDTO());
        }
        return postingDTOS;
    }

}
