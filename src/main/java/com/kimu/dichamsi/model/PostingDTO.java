//package com.kimu.dichamsi.model;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.persistence.*;
//import java.util.ArrayList;
//import java.util.List;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//@Builder
//public class PostingDTO {
//    private Long id;
//    //작성자(닉네임)
//    private String nickname;
//    //제목
//    private String title;
//    //상세 내용
//    private String content;
//    //좋아요 수
//    private long liked;
//    private List<PostingImages> images = new ArrayList<>();
//    private List<Comment> comments = new ArrayList<>();
//
//    public Posting toEntity(){
//        return Posting.builder()
//                .id(id)
//                .title(title)
//                .content(content)
//                .liked(liked)
//                .images(images)
//                .comments(comments)
//                .build();
//    }
//}
