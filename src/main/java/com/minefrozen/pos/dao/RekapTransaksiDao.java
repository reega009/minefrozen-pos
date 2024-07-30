package com.minefrozen.pos.dao;

import com.minefrozen.pos.dto.RekapTransaksiDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("posJdbc")
    private NamedParameterJdbcTemplate jdbcTemplate;

    public List<RekapTransaksiDto.Transaksi> findAll(Integer idStore, Integer nomorKasir, Integer shift, Date tanggal){
        String query = "select \n" +
                "\tcoalesce((select distinct\n" +
                "\t\ttsal.modal_awal\n" +
                "\tfrom\n" +
                "\t\ttmsaldoawal tsal\n" +
                "\twhere\n" +
                "\t\ttsal.id_store = :idStore\n" +
                "\t\tand tsal.nomor_kasir = :nomorKasir\n" +
                "\t\tand tsal.d_pgun_rekam::date = :tanggal),0) as modalKasir,\n" +
                "\tcoalesce(\n" +
                "\t\t(select\n" +
                "\t\t\tsum(tr.total_return)\n" +
                "\t\tfrom\n" +
                "\t\t\ttmreturn tr\n" +
                "\t\twhere\n" +
                "\t\t\ttr.id_store = :idStore\n" +
                "\t\t\tand tr.nomor_kasir = :nomorKasir\n" +
                "\t\t\tand tr.d_pgun_rekam::date = :tanggal)\n" +
                "\t,0) as returnKasir,\n" +
                "\tcoalesce(SUM(CASE WHEN jenis_pembayaran = 1 THEN total_harga ELSE 0 END),0) AS cash,\n" +
                "\tcoalesce(SUM(CASE WHEN jenis_pembayaran IN (2,5) THEN total_harga ELSE 0 END),0) AS debit,\n" +
                "\tcoalesce(SUM(CASE WHEN jenis_pembayaran = 3 THEN total_harga ELSE 0 END),0) AS credit,\n" +
                "\tcoalesce(\n" +
                "\t\tsum(CASE WHEN jenis_pembayaran in (1,2,3,5) THEN total_harga ELSE 0 END) -\n" +
                "\t\tcoalesce(\n" +
                "\t\t\t(select\n" +
                "\t\t\t\tsum(tr.total_return)\n" +
                "\t\t\tfrom\n" +
                "\t\t\t\ttmreturn tr\n" +
                "\t\t\twhere\n" +
                "\t\t\t\ttr.id_store = :idStore\n" +
                "\t\t\t\tand tr.nomor_kasir = :nomorKasir\n" +
                "\t\t\t\tand tr.d_pgun_rekam::date = :tanggal)\n" +
                "\t\t,0)\n" +
                "\t,0) as total\n" +
                "from\n" +
                "\ttmtransaksi trx\n" +
                "where\n" +
                "\ttrx.id_store = :idStore\n" +
                "\tand trx.nomor_kasir = :nomorKasir\n" +
                "\tand trx.d_pgun_rekam::date = :tanggal\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idStore", idStore);
        map.addValue("nomorKasir", nomorKasir);
        map.addValue("tanggal", tanggal);
        map.addValue("shift", shift);
        return jdbcTemplate.query(query, map, new BeanPropertyRowMapper<>(RekapTransaksiDto.Transaksi.class));
    }


}
