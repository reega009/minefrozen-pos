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
        private BigInteger barcode;
    }

}
