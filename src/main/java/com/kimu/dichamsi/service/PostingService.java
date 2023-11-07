package com.kimu.dichamsi.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.kimu.dichamsi.model.Comment;
import com.kimu.dichamsi.model.Posting;
import com.kimu.dichamsi.model.PostingDTO;
import com.kimu.dichamsi.model.PostingImages;
import com.kimu.dichamsi.repository.MemberReository;
import com.kimu.dichamsi.repository.PostingImagesRepository;
import com.kimu.dichamsi.repository.PostingRepositoy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class PostingService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${spring.servlet.multipart.location}")
    private String fileUploadLocation;
    private final PostingRepositoy postingRepositoy;
    private final MemberReository memberReository;
    private final PostingImagesRepository postingImagesRepository;

    public PostingService(AmazonS3 amazonS3, PostingRepositoy postingRepositoy, MemberReository memberReository, PostingImagesRepository postingImagesRepository) {
        this.amazonS3 = amazonS3;
        this.postingRepositoy = postingRepositoy;
        this.memberReository = memberReository;
        this.postingImagesRepository = postingImagesRepository;
    }

    public void savePosting(PostingDTO postingDTO,
                            MultipartFile[] images,
                            String nickname) throws IOException {
        log.info("log info={}",nickname);
        Posting postingEntity = postingDTO.toEntity();
        memberReository.findByNickname(nickname).ifPresent(postingEntity::setMember);
        log.info("log info={}",postingDTO);
        postingRepositoy.save(postingEntity);

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

                postingImagesRepository.save(postingImages);
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

    public PostingDTO showView(Long id){
        Optional<Posting> optionalPosting = postingRepositoy.findById(id);
        if(optionalPosting.isPresent()){
            Posting posting = optionalPosting.get();
            return posting.toDTO();
        }else{
            return null;
        }
    }

    public void addNewPostingImages(PostingDTO postingDTO, MultipartFile[] newImages, String nickname) throws IOException {
        Posting postingEntity = postingDTO.toEntity();

        // 새 이미지 업로드 로직 추가
        if (!Objects.isNull(newImages)) {
            for (MultipartFile image : newImages) {
                String originalFilename = image.getOriginalFilename();
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(image.getSize());
                metadata.setContentType(image.getContentType());

                amazonS3.putObject(bucket, originalFilename, image.getInputStream(), metadata);

                PostingImages postingImages = PostingImages.builder()
                        .images(amazonS3.getUrl(bucket, originalFilename).toString())
                        .posting(postingEntity)
                        .build();

                postingImagesRepository.save(postingImages);
            }
        }
    }

    public boolean imageDelete(Long imageId){
        // 이미지를 찾아서 삭제
        PostingImages image = postingImagesRepository.findById(imageId)
                .orElse(null);

        if (image != null) {
            postingImagesRepository.delete(image);
            return true; // 삭제 성공
        } else {
            return false; // 삭제 실패
        }
    }

    public void postingDelete(Long id){
        postingRepositoy.deleteById(id);
    }



}
