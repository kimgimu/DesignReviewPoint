package com.kimu.dichamsi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@Data
@Builder
public class CommentDTO {

    private String nickname;
    private String content;
    private LocalDate date;

    public Comment toEntity(Member member,Post post) {
        return Comment.builder()
                .member(member)
                .content(content)
                .date(date)
                .member(member)
                .post(post)
                .build();
    }

}
