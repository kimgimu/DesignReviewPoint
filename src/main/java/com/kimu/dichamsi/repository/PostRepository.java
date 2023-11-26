package com.kimu.dichamsi.repository;

import com.kimu.dichamsi.model.Post;
import com.kimu.dichamsi.model.PostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    @Query(value = "SELECT post_id, " +
            "SUBSTRING_INDEX(SUBSTRING_INDEX(content, 'src=\"', -1), '\"', 1) AS content, " +
            "liked, " +
            "title, " +
            "type, " +
            "member_id " +
            "FROM post", nativeQuery = true)
    List<Post> findAllPostWithImageUrls(Pageable pageable);

    @Query(value = "SELECT post_id, " +
            "SUBSTRING_INDEX(SUBSTRING_INDEX(content, 'src=\"', -1), '\"', 1) AS content, " +
            "liked, " +
            "title, " +
            "type, " +
            "member_id " +
            "FROM post " +
            "WHERE title LIKE %:keyword% OR content LIKE %:keyword% OR type LIKE %:keyword% OR:keyword", nativeQuery = true)
    List<Post> findSearchAllPostWithImageUrls(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT post_id, " +
            "SUBSTRING_INDEX(SUBSTRING_INDEX(content, 'src=\"', -1), '\"', 1) AS content, " +
            "liked, " +
            "title, " +
            "type, " +
            "member_id " +
            "FROM post"
            , nativeQuery = true)
    List<Post> findTop7ByLikedOrderByIdDesc();



}
