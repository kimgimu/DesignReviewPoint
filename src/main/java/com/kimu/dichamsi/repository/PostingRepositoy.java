package com.kimu.dichamsi.repository;

import com.kimu.dichamsi.model.Posting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostingRepositoy extends JpaRepository<Posting,Long> {
    @Query("SELECT DISTINCT p FROM Posting p LEFT JOIN FETCH p.images")
    List<Posting> findAllWithImages();
}
