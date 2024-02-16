package com.minefrozen.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public class LaporanCashierDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LaporanCashier{
        private String tanggal;
        private BigDecimal cash;
        private BigDecimal debit;
        private BigDecimal credit;
        private BigDecimal piutang;
        private BigDecimal returns;
        private BigDecimal total;
    }

}
