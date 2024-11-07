package com.example.memory_auth_microservice.dao;

import com.example.memory_auth_microservice.model.MemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberDao extends JpaRepository<MemEntity, Integer> {
    @Query("select mem from MemEntity mem where mem.memEmail = :email")
    MemEntity findByMemEmail(String email);
}
