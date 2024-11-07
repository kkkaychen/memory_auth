package com.example.memory_auth_microservice.model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "mem", schema = "cga103g1", catalog = "")
public class MemEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "mem_no")
    private int memNo;
    @Basic
    @Column(name = "mem_acc")
    private String memAcc;
    @Basic
    @Column(name = "mem_pwd")
    private String memPwd;
    @Basic
    @Column(name = "acc_status")
    private byte accStatus;
    @Basic
    @Column(name = "mem_name")
    private String memName;
    @Basic
    @Column(name = "mem_gender")
    private String memGender;
    @Basic
    @Column(name = "mem_email")
    private String memEmail;
    @Basic
    @Column(name = "mem_mobile")
    private String memMobile;
    @Basic
    @Column(name = "mem_city")
    private String memCity;
    @Basic
    @Column(name = "mem_dist")
    private String memDist;
    @Basic
    @Column(name = "mem_addr")
    private String memAddr;
    @Basic
    @Column(name = "mem_reg_date")
    private Timestamp memRegDate;
    @Basic
    @Column(name = "mem_pic")
    private byte[] memPic;
    @Basic
    @Column(name = "mem_report_count")
    private Byte memReportCount;
    @Basic
    @Column(name = "mem_card")
    private String memCard;

    public int getMemNo() {
        return memNo;
    }

    public void setMemNo(int memNo) {
        this.memNo = memNo;
    }

    public String getMemAcc() {
        return memAcc;
    }

    public void setMemAcc(String memAcc) {
        this.memAcc = memAcc;
    }

    public String getMemPwd() {
        return memPwd;
    }

    public void setMemPwd(String memPwd) {
        this.memPwd = memPwd;
    }

    public byte getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(byte accStatus) {
        this.accStatus = accStatus;
    }

    public String getMemName() {
        return memName;
    }

    public void setMemName(String memName) {
        this.memName = memName;
    }

    public String getMemGender() {
        return memGender;
    }

    public void setMemGender(String memGender) {
        this.memGender = memGender;
    }

    public String getMemEmail() {
        return memEmail;
    }

    public void setMemEmail(String memEmail) {
        this.memEmail = memEmail;
    }

    public String getMemMobile() {
        return memMobile;
    }

    public void setMemMobile(String memMobile) {
        this.memMobile = memMobile;
    }

    public String getMemCity() {
        return memCity;
    }

    public void setMemCity(String memCity) {
        this.memCity = memCity;
    }

    public String getMemDist() {
        return memDist;
    }

    public void setMemDist(String memDist) {
        this.memDist = memDist;
    }

    public String getMemAddr() {
        return memAddr;
    }

    public void setMemAddr(String memAddr) {
        this.memAddr = memAddr;
    }

    public Timestamp getMemRegDate() {
        return memRegDate;
    }

    public void setMemRegDate(Timestamp memRegDate) {
        this.memRegDate = memRegDate;
    }

    public byte[] getMemPic() {
        return memPic;
    }

    public void setMemPic(byte[] memPic) {
        this.memPic = memPic;
    }

    public Byte getMemReportCount() {
        return memReportCount;
    }

    public void setMemReportCount(Byte memReportCount) {
        this.memReportCount = memReportCount;
    }

    public String getMemCard() {
        return memCard;
    }

    public void setMemCard(String memCard) {
        this.memCard = memCard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemEntity memEntity = (MemEntity) o;
        return memNo == memEntity.memNo && accStatus == memEntity.accStatus && Objects.equals(memAcc, memEntity.memAcc) && Objects.equals(memPwd, memEntity.memPwd) && Objects.equals(memName, memEntity.memName) && Objects.equals(memGender, memEntity.memGender) && Objects.equals(memEmail, memEntity.memEmail) && Objects.equals(memMobile, memEntity.memMobile) && Objects.equals(memCity, memEntity.memCity) && Objects.equals(memDist, memEntity.memDist) && Objects.equals(memAddr, memEntity.memAddr) && Objects.equals(memRegDate, memEntity.memRegDate) && Arrays.equals(memPic, memEntity.memPic) && Objects.equals(memReportCount, memEntity.memReportCount) && Objects.equals(memCard, memEntity.memCard);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(memNo, memAcc, memPwd, accStatus, memName, memGender, memEmail, memMobile, memCity, memDist, memAddr, memRegDate, memReportCount, memCard);
        result = 31 * result + Arrays.hashCode(memPic);
        return result;
    }
}
