package com.minefrozen.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
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
        private Integer berat;
        private String barcode;
        private Integer qtyInven;
        private Boolean isDiskon;
        private String jenisDiskon;
        //        Jenis Disc
        private BigDecimal discProduk;
        //        Jenis Beli 2 Gratis 1
        private Integer idProdukBonus;
        private Integer minQtyToGetBonus;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Produk{

        private Integer id;
        private String kodeProduct;
        private String namaProduct;
        private BigInteger barcode;
        private Integer idSupplier;
        private Integer idCategory;
        private Integer berat;
        private BigDecimal hargaBeli;
        private BigDecimal hargaJual;
        private Integer minJumlahBarang;
        private String descProduk;
        private Integer iPgunRekam;
        private Timestamp dPgunRekam;
        private Integer iPgunUbah;
        private Timestamp dPgunUbah;
    }

}
