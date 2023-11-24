//package com.kimu.dichamsi.model;
//
//import lombok.*;
//
//import javax.persistence.*;
//
//@Entity
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@ToString(exclude = "posting")
//@Builder
//public class PostingImages {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name="POSTING_IMAGES_ID")
//    private Long id;
//
//    //이미지 경로 S3
//    private String images;
//    @ManyToOne
//    @JoinColumn(name="POSTING_ID", referencedColumnName = "POSTING_ID")
//    private Posting posting;
//
//}
