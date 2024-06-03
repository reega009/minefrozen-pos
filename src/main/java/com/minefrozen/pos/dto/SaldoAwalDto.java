package com.minefrozen.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class SaldoAwalDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SaldoAwal {
        private Integer id;
        private BigDecimal modalAwal;
        private Integer nomorKasir;
        private Integer idStore;
        private Integer iPgunRekam;
        private Timestamp dPgunRekam;
    }

}
