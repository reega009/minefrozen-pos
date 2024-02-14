package com.minefrozen.pos.dao;

import com.minefrozen.pos.dto.RekapTransaksiDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
@Slf4j
public class RekapTransaksiDao {


    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public List<RekapTransaksiDto.Transaksi> findAll(Integer idStore, Integer nomorKasir, Integer shift, Date tanggal){
        String query = "select \n" +
                "\t0 as modalKasir,\n" +
                "\t0 as returnKasir,\n" +
                "\tcoalesce(SUM(CASE WHEN jenis_pembayaran = 1 THEN total_harga ELSE 0 END),0) AS cash,\n" +
                "\tcoalesce(SUM(CASE WHEN jenis_pembayaran = 2 THEN total_harga ELSE 0 END),0) AS debit,\n" +
                "\tcoalesce(SUM(CASE WHEN jenis_pembayaran = 3 THEN total_harga ELSE 0 END),0) AS credit,\n" +
                "\tcoalesce(sum(CASE WHEN jenis_pembayaran in (1,2,3) THEN total_harga ELSE 0 END),0) as total\n" +
                "from\n" +
                "\ttmtransaksi trx\n" +
                "where\n" +
                "\tid_store = :idStore\n" +
                "\tand nomor_kasir = :nomorKasir\n" +
                "\tand shift = :shift\n" +
                "\tand d_pgun_rekam::date = :tanggal\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idStore", idStore);
        map.addValue("nomorKasir", nomorKasir);
        map.addValue("tanggal", tanggal);
        map.addValue("shift", shift);
        return jdbcTemplate.query(query, map, new BeanPropertyRowMapper<>(RekapTransaksiDto.Transaksi.class));
    }


}
