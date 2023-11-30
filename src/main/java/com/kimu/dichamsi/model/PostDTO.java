package com.kimu.dichamsi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostDTO {
    private String nickname;
    private String title;
    private String content;
    private Tag type;

    public Post toEntity(Member member, List<Comment> commentList){
        return Post.builder()
                .member(member)
                .title(title)
                .content(content)
                .type(type)
                .comments(commentList)
                .build();
    }

    public Post toEntity(Member member){
        return Post.builder()
                .member(member)
                .title(title)
                .content(content)
                .type(type)
                .build();
    }
}
