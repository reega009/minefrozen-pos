package com.minefrozen.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class MemberDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TambahMember{
        private Integer id;
        private Integer idStore;
        private String kodeMember;
        private String namaMember;
        private String nomorHandphone;
        private BigDecimal discMember;
        private Integer iPgunRekam;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Member{
        private Integer id;
        private Integer idStore;
        private String kodeMember;
        private String namaMember;
        private String nomorHandphone;
        private Boolean aktif;
        private BigDecimal discMember;
        private Integer iPgunRekam;
        private Timestamp dPgunRekam;
        private Integer iPgunUbah;
        private Timestamp dPgunUbah;
    }

}
