package com.kimu.dichamsi.repository;

import com.kimu.dichamsi.model.Posting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostingRepositoy extends JpaRepository<Posting,Long> {
}
