package com.kimu.dichamsi.repository;

import com.kimu.dichamsi.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberReository extends JpaRepository<Member,Long> {
    //이메일 중복체크
    Optional<Member> findByUserEmail(String userEmail);
}
