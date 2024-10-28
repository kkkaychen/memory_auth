package com.example.memory_auth_microservice.service;

import com.example.memory_auth_microservice.dao.EmpDao;
import com.example.memory_auth_microservice.model.EmpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class MyUserDetailService implements UserDetailsService {

    private final EmpDao empDao;

    @Autowired
    public MyUserDetailService(EmpDao empDao) {
        this.empDao = empDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        EmpEntity emp = empDao.findByEmpAcc(username);
        if (emp == null) {
            throw new UsernameNotFoundException("找不到使用者： " + username);
        }

        // 判斷 empState 是否為啟用狀態
        boolean enabled = emp.getEmpState() == 1;

        return new User(emp.getEmpAcc(),
                emp.getEmpPwd(),
                enabled,          // 根據 empState 設定是否啟用
                true,             // 帳號是否過期
                true,             // 憑證是否過期
                true,             // 帳號是否被鎖定
                Collections.emptyList());  // 使用空的權限列表

    }
}
