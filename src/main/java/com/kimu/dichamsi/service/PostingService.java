package com.kimu.dichamsi.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.kimu.dichamsi.model.*;
import com.kimu.dichamsi.repository.MemberReository;
import com.kimu.dichamsi.repository.PostRepository;
import com.kimu.dichamsi.repository.PostingImagesRepository;
import com.kimu.dichamsi.repository.PostingRepositoy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class PostingService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final PostingRepositoy postingRepositoy;
    private final MemberReository memberReository;
    private final PostingImagesRepository postingImagesRepository;
    private final PostRepository postRepository;

    public PostingService(AmazonS3 amazonS3, PostingRepositoy postingRepositoy, MemberReository memberReository, PostingImagesRepository postingImagesRepository, PostRepository postRepository) {
        this.amazonS3 = amazonS3;
        this.postingRepositoy = postingRepositoy;
        this.memberReository = memberReository;
        this.postingImagesRepository = postingImagesRepository;
        this.postRepository = postRepository;
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

    public String imageUpload(MultipartRequest request) throws IOException {
        //인자에서 파일 뽑아내는 과정
        MultipartFile file = request.getFile("upload");

        //뽑아낸 이미지에서 이름 및 확장자를 추출
        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.indexOf(".")); //확장자

        //고유한 이름으로 변경
        String uuidFileName = UUID.randomUUID() + fileName;

        //메타데이터에 정보저장
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(ext);

        amazonS3.putObject(bucket,uuidFileName,file.getInputStream(),metadata);

        return amazonS3.getUrl(bucket,uuidFileName).toString();
    }

    public Post writePost(PostDTO postDTO){
       Optional<Member> member = memberReository.findByNickname(postDTO.getNickname());
       return postRepository.save(postDTO.toEntity(member.get()));
    }

    public List<Post> viewAllPost(int page, int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return postRepository.findAllPostWithImageUrls(pageable);
    }

    public Optional<Post> viewPost(Long id){
        return postRepository.findById(id);
    }

}
