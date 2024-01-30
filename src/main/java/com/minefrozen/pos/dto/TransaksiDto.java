package com.minefrozen.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class TransaksiDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TambahTransaksi{
        private Integer id;
        private Integer idStore;
        private Integer jenisPembayaran;
        private Integer nomorKasir;
        private String kodeTransaksi;
        private Integer iPgunRekam;
        private Timestamp dPgunRekam;
        private BigDecimal totalHargaPerTransaksi;
        private Integer idMember;
        private Integer discMember;
        private List<TambahTransaksiRinci> transaksiRinci;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TambahTransaksiRinci{
        private Integer idTransaksi;
        private Integer idStore;
        private Integer idProduk;
        private Date expiredDate;
        private Integer qty;
        private BigDecimal hargaJual;
        private Integer discProduk;
        private BigDecimal totalHargaPerProduk;
    }

}
