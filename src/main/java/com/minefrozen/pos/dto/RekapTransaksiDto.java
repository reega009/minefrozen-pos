package com.minefrozen.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

public class RekapTransaksiDto {


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Transaksi{
        private BigDecimal modalKasir;
        private BigDecimal returnKasir;
        private BigDecimal cash;
        private BigDecimal debit;
        private BigDecimal credit;
        private BigDecimal total;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TransaksiRinci{
        private String jenisPembayaran;
        private String kodeProduk;
        private String namaProduk;
        private BigDecimal hargaJual;
        private BigDecimal discProduk;
        private Integer qty;
        private BigDecimal totalHargaPerProduk;
        //
        private String kodeTransaksi;
        private String tanggalTransaksi;
        private String nomorKasir;
        private String namaKasir;
        private BigDecimal discMember;
        private BigDecimal totalHarga;

    }


}
