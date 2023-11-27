package com.kimu.dichamsi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    private LocalDate date;

}
