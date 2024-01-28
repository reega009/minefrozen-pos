package com.minefrozen.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

public class ProdukDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProdukKasir{
        private Integer id;
        private String kodeProduct;
        private String namaProduct;
        private BigDecimal hargaJual;
        private BigDecimal diskon;
        private Integer berat;
        private String barcode;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Produk{

        private Integer id;
        private String kodeProduct;
        private String namaProduct;
        private BigInteger barcode;
        private Integer berat;
        private BigDecimal hargaBeli;
        private BigDecimal hargaJual;
        private Integer minJumlahBarang;
        private Integer iPgunRekam;
        private Timestamp dPgunRekam;
        private Integer iPgunUbah;
        private Timestamp dPgunUbah;
    }

}
