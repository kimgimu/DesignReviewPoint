package com.kimu.dichamsi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostDTO {
    private String nickname;
    private String title;
    private String content;
    private Tag type;

    public Post toEntity(Member member){
        return Post.builder()
                .member(member)
                .title(title)
                .content(content)
                .type(type)
                .build();
    }
}
