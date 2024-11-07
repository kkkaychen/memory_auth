package com.example.memory_auth_microservice.service;

import com.example.memory_auth_microservice.dao.MemberDao;
import com.example.memory_auth_microservice.model.MemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberDao memberDao;

    @Autowired
    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemEntity getMember(String email) {
       return memberDao.findByMemEmail(email);
    }
}
