package com.kimu.dichamsi.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.kimu.dichamsi.model.*;
import com.kimu.dichamsi.repository.MemberReository;
import com.kimu.dichamsi.repository.PostRepository;
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

    private final MemberReository memberReository;
    private final PostRepository postRepository;

    public PostingService(AmazonS3 amazonS3, MemberReository memberReository, PostRepository postRepository) {
        this.amazonS3 = amazonS3;
        this.memberReository = memberReository;
        this.postRepository = postRepository;
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

        amazonS3.putObject(bucket, uuidFileName, file.getInputStream(), metadata);

        return amazonS3.getUrl(bucket, uuidFileName).toString();
    }

    public Post writePost(PostDTO postDTO) {
        Optional<Member> member = memberReository.findByNickname(postDTO.getNickname());
        return postRepository.save(postDTO.toEntity(member.get()));
    }

    public List<Post> viewAllPost(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return postRepository.findAllPostWithImageUrls(pageable);
    }

    public List<Post> viewSearchPost(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return postRepository.findSearchAllPostWithImageUrls(keyword, pageable);
    }

    public Optional<Post> viewPost(Long id) {
        return postRepository.findById(id);
    }

    public Post likeButton(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        Long liked = post.get().getLiked();
        log.info("현재 라이크숫자 = {}", liked);
        post.get().setLiked(liked + 1);
        log.info("set한 라이크 숫자 = {}", liked);
        return postRepository.save(post.get());
    }

    public Post UpdatePost(PostDTO postDTO, Long postId) {
        log.info("서비스단 확인={}");
        Optional<Member> member = memberReository.findByNickname(postDTO.getNickname());
        log.info("서비스단 확인2={}", member.get().toString());
        Post post = postDTO.toEntity(member.get());
        log.info("서비스단 확인3={}", post);
        post.setId(postId);
        log.info("서비스단 확인4={}", post);
        Post post1 = postRepository.save(post);
        log.info("서비스단 확인5={}", post1);
        return post1;
    }

    public Optional<Post> deletePost(Long id) {
        postRepository.deleteById(id);
        return postRepository.findById(id);
    }

    public List<TopSeven> topSevenPost() {
        List<TopSeven> topSeven = new ArrayList<>();
        List<Post> postList = postRepository.findTop7ByLikedOrderByIdDesc();

        for (int i = 0; i < postList.size(); i++) {
            switch (i) {
                case 0:
                    topSeven.add(new TopSeven("hideLeft", postList.get(i).getContent()));
                    break;
                case 1:
                    topSeven.add(new TopSeven("prevLeftSecond", postList.get(i).getContent()));
                    break;
                case 2:
                    topSeven.add(new TopSeven("prev", postList.get(i).getContent()));
                    break;
                case 3:
                    topSeven.add(new TopSeven("selected", postList.get(i).getContent()));
                    break;
                case 4:
                    topSeven.add(new TopSeven("next", postList.get(i).getContent()));
                    break;
                case 5:
                    topSeven.add(new TopSeven("nextRightSecond", postList.get(i).getContent()));
                    break;
                case 6:
                    topSeven.add(new TopSeven("hideRight", postList.get(i).getContent()));
                    break;
            }
        }

        return topSeven;
    }
}
