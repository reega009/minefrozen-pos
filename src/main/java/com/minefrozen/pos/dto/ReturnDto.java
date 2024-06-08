package com.minefrozen.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class ReturnDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FindAllReturn{
        private Integer id;
        private String kodeTransaksi;
        private String namaProduk;
        private Date expiredDate;
        private BigDecimal hargaJual;
        private BigDecimal discProduk;
        private BigDecimal qty;
        private String namaUser;
        private String tanggalReturn;
        private BigDecimal totalReturn;
        private Integer nomorKasir;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Return{
        private Integer id;
        private Integer idStore;
        private Integer idTransaksi;
        private Integer idProduk;
        private Date expiredDate;
        private BigDecimal hargaJual;
        private BigDecimal discProduk;
        private BigDecimal qty;
        private Integer nomorKasir;
        private Integer iPgunRekam;
        private Timestamp dPgunRekam;
        private BigDecimal totalReturn;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FindTransaksiReturn{
        private Integer idTransaksi;
        private String kodeTransaksi;
        private Integer idProduk;
        private String namaProduk;
        private BigDecimal qty;
        private BigDecimal hargaJual;
        private BigDecimal discMember;
        private BigDecimal discProduk;
        private Integer idStore;
        private String tanggalTransaksi;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Inventory{
        private Integer id;
        private Integer idProduk;
        private Integer idStore;
        private BigDecimal qty;
        private Date expiredDate;
        private Integer iPgunRekam;
        private Date dPgunRekam;
        private Integer iPgunUbah;
        private Date dPgunUbah;
    }

}
