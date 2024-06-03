package com.minefrozen.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public class ReportDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LaporanPendapatan{
        private String kassa;
        private String tanggal;
        private BigDecimal saldoawal;
        private BigDecimal cash;
        private BigDecimal debit;
        private BigDecimal credit;
        private BigDecimal piutang;
        private BigDecimal refund;
        private BigDecimal subtotal;
        private BigDecimal totalpendapatan;
        private BigDecimal totalcash;
    }

}
