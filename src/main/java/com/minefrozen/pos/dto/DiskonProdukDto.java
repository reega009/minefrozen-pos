package com.minefrozen.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class DiskonProdukDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DiskonProduk{
        private Integer id;
        private Integer idStore;
        private Integer idProduk;
        private String namaProduk;
        private String jenisDiskon;
        private Date tanggalAwalPeriode;
        private Date tanggalAkhirPeriode;
        private Integer iPgunRekam;
        private Timestamp dPgunRekam;
        private Integer iPgunUbah;
        private Timestamp dPgunUbah;
//        Jenis Disc
        private BigDecimal discProduk;
//        Jenis Beli 2 Gratis 1
        private Integer idProdukBonus;
        private String namaProdukBonus;
        private Integer minQtyToGetBonus;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ParamCheckDiskon{
        private Integer idStore;
        private Integer idProduk;
        private BigDecimal qtyBeli;
    }

}
