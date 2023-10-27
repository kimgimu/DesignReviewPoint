package com.kimu.dichamsi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Posting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="POSTING_ID")
    private Long id;

    private String title;
    private String content;
    private Long liked;

    @OneToMany(mappedBy = "posting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostingImages> images = new ArrayList<>();

    public PostingDTO toDTO(){
        return PostingDTO.builder()
                .title(title)
                .content(content)
                .liked(liked)
                .images(images)
                .build();
    }

}
