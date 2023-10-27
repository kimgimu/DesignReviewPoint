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
public class PostingImages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="POSTING_IMAGES_ID")
    private Long id;

//    //사용자 지정 파일 이름
//    private String uploadFileName;
//    //서버 지정 파일 이름(UUID 사용)
//    private String storedFileName;
    //이미지 경로 S3
    private String images;
    @ManyToOne
    @JoinColumn(name="POSTING_ID", referencedColumnName = "POSTING_ID")
    private Posting posting;

}
