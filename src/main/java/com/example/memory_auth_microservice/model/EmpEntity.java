package com.example.memory_auth_microservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "emp", schema = "cga103g1", catalog = "")
public class EmpEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "emp_no")
    private int empNo;
    @Basic
    @Column(name = "emp_acc")
    private String empAcc;
    @Basic
    @Column(name = "emp_pwd")
    private String empPwd;
    @Basic
    @Column(name = "emp_name")
    private String empName;
    @Basic
    @Column(name = "emp_email")
    private String empEmail;
    @Basic
    @Column(name = "emp_state")
    private byte empState;
}
