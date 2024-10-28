package com.example.memory_auth_microservice.dao;

import com.example.memory_auth_microservice.model.EmpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpDao extends JpaRepository<EmpEntity, Integer> {
    @Query("select emp from EmpEntity emp where emp.empAcc = :username")
    EmpEntity findByEmpAcc(String username);
}
