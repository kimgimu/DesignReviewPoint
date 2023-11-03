package com.kimu.dichamsi.model;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "posting")
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="COMENT_ID")
    private Long id;
    private String content;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "POSTING_ID")
    private Posting posting;

    public CommentDTO toDTO(){
        return CommentDTO.builder()
                .commentId(id)
                .content(content)
                .memberName(member.getNickname())
                .postingId(posting.getId())
                .build();
    }

}
