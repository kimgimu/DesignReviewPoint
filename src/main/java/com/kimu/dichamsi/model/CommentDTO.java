package com.kimu.dichamsi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@Data
@Builder
public class CommentDTO {

    private Long commentId;
    private String content;
    private String memberName;
    private Long postingId;

    public Comment toEntity() {
        return Comment.builder()
                .id(commentId)
                .content(content)
                .member(Member.builder().nickname(memberName).build())
                .posting(Posting.builder().id(postingId).build())
                .build();
    }

}
