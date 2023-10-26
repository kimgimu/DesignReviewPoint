package com.kimu.dichamsi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PostingImages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="POSTING_IMAGES_ID")
    private Long id;

    //사용자 지정 파일 이름
    private String uploadFileName;
    private String storedFileName;
    private String fullPath;
    @ManyToOne
    @JoinColumn(name="POSTING_ID")
    private Posting posting;

}
