package com.kimu.dichamsi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostingDTO {

    //제목
    private String title;
    //상세 내용
    private String content;
    //좋아요 수
    private long liked;

    public Posting toEntity(){
        return Posting.builder()
                .title(title)
                .content(content)
                .liked(liked)
                .build();
    }
}
