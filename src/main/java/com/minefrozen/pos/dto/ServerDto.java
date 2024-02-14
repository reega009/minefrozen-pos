package com.minefrozen.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class ServerDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TambahPiutang{
        private Integer id;
        private Integer idTransaksi;
        private BigDecimal totalPelunasan;
        private Integer idStore;
    }

}
