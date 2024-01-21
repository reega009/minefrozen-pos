package com.minefrozen.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
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
        private List<TambahTransaksiRinci> transaksiRinci;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TambahTransaksiRinci{
        private Integer idTransaksi;
        private Integer idProduk;
        private Date expiredDate;
        private Integer qty;
    }

}
