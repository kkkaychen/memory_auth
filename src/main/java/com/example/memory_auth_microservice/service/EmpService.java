package com.example.memory_auth_microservice.service;

import com.example.memory_auth_microservice.dao.EmpDao;
import com.example.memory_auth_microservice.model.EmpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpService {
    private final EmpDao empDao;

    @Autowired
    public EmpService(EmpDao empDao) {
        this.empDao = empDao;
    }
    public EmpEntity getEmp(String empAcc) {
        return empDao.findByEmpAcc(empAcc);
    }
}
