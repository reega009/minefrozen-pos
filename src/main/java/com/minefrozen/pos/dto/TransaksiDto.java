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
        private Integer shift;
        private String kodeTransaksi;
        private Integer iPgunRekam;
        private Timestamp dPgunRekam;
        private BigDecimal totalHargaPerTransaksi;
        private Integer idMember;
        private Integer discMember;
        private Date tanggalTenggatPiutang;
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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Transaksi{
        private Integer id;
        private Integer idStore;
        private Integer jenisPembayaran;
        private String namaPembayaran;
        private Integer nomorKasir;
        private Integer shift;
        private String kodeTransaksi;
        private BigDecimal totalHargaPerTransaksi;
        private Integer idMember;
        private Integer discMember;
        private Date tanggalTenggatPiutang;
        private Integer iPgunRekam;
        private String namaUser;
        private String tanggalTransaksi;
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
