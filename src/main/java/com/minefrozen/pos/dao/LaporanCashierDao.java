package com.minefrozen.pos.dao;

import com.minefrozen.pos.dto.LaporanCashierDto;
import com.minefrozen.pos.dto.RekapTransaksiDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public class LaporanCashierDao {

    @Autowired
    @Qualifier("posJdbc")
    private NamedParameterJdbcTemplate jdbcTemplate;

    public List<LaporanCashierDto.LaporanCashier> findAll(Integer idStore, Date tanggalAwal, Date tanggalAkhir){
        String query = "select\n" +
                "    TO_CHAR(tanggal::date, 'DD/MM/YYYY') as tanggal,\n" +
                "    sum(cash) as cash,\n" +
                "    sum(debit) as debit,\n" +
                "    sum(credit) as credit,\n" +
                "    sum(piutang) as piutang,\n" +
                "    sum(return) as returns,\n" +
                "    sum(cash + debit + credit + piutang - return) as total\n" +
                "FROM (\n" +
                "\t    SELECT\n" +
                "\t        d_pgun_rekam::date as tanggal,\n" +
                "\t        SUM(total_harga) as cash,\n" +
                "\t        0 as debit,\n" +
                "\t        0 as credit,\n" +
                "\t        0 as piutang,\n" +
                "\t        0 as return\n" +
                "\t    FROM \n" +
                "\t        tmtransaksi trx \n" +
                "\t        LEFT JOIN tmtransaksirinci trxrinci ON trx.id_store  = trxrinci.id_transaksi\n" +
                "\t    WHERE\n" +
                "\t        d_pgun_rekam::date BETWEEN :tanggalAwal AND :tanggalAkhir\n" +
                "\t        AND jenis_pembayaran = 1\n" +
                "\t        and trx.id_store = :idStore\n" +
                "\t    GROUP BY d_pgun_rekam\n" +
                "    UNION ALL\n" +
                "\t    SELECT\n" +
                "\t        d_pgun_rekam::date as tanggal,\n" +
                "\t        0 as cash,\n" +
                "\t        SUM(total_harga) as debit,\n" +
                "\t        0 as credit,\n" +
                "\t        0 as piutang,\n" +
                "\t        0 as return\n" +
                "\t    FROM \n" +
                "\t        tmtransaksi trx \n" +
                "\t        LEFT JOIN tmtransaksirinci trxrinci ON trx.id_store  = trxrinci.id_transaksi\n" +
                "\t    WHERE\n" +
                "\t        d_pgun_rekam::date BETWEEN :tanggalAwal AND :tanggalAkhir\n" +
                "\t        AND jenis_pembayaran IN (2,5)\n" +
                "\t        and trx.id_store = :idStore\n" +
                "\t    GROUP BY d_pgun_rekam\n" +
                "    union all\n" +
                "    \tselect\n" +
                "\t\t\td_pgun_rekam::date as tanggal,\n" +
                "\t\t\t0 as cash,\n" +
                "\t\t\t0 as debit,\n" +
                "\t\t\tsum(total_harga) as credit,\n" +
                "\t\t\t0 as piutang,\n" +
                "\t\t\t0 as return\n" +
                "\t\tfrom \n" +
                "\t\t\ttmtransaksi trx \n" +
                "\t\t\tleft join tmtransaksirinci trxrinci on trx.id_store  = trxrinci.id_transaksi\n" +
                "\t\twhere\n" +
                "\t\t\td_pgun_rekam::date between :tanggalAwal and :tanggalAkhir\n" +
                "\t\t\tand jenis_pembayaran = 3\n" +
                "\t        and trx.id_store = :idStore\n" +
                "\t\tgroup by d_pgun_rekam\n" +
                "\tunion all\n" +
                "\t\tselect\n" +
                "\t\t\td_pgun_rekam::date as tanggal,\n" +
                "\t\t\t0 as cash,\n" +
                "\t\t\t0 as debit,\n" +
                "\t\t\t0 as credit,\n" +
                "\t\t\tsum(total_harga) as piutang,\n" +
                "\t\t\t0 as return\n" +
                "\t\tfrom \n" +
                "\t\t\ttmtransaksi trx \n" +
                "\t\t\tleft join tmtransaksirinci trxrinci on trx.id_store  = trxrinci.id_transaksi\n" +
                "\t\twhere\n" +
                "\t\t\td_pgun_rekam::date between :tanggalAwal and :tanggalAkhir\n" +
                "\t\t\tand jenis_pembayaran = 4\n" +
                "\t        and trx.id_store = :idStore\n" +
                "\t\tgroup by d_pgun_rekam\n" +
                "\tunion all\n" +
                "\t\tselect\n" +
                "\t\t\ttrx.d_pgun_rekam::date as tanggal,\n" +
                "\t\t\t0 as cash,\n" +
                "\t\t\t0 as debit,\n" +
                "\t\t\t0 as credit,\n" +
                "\t\t\t0 as piutang,\n" +
                "\t\t\tsum(tr.total_return) as return\n" +
                "\t\tfrom \n" +
                "\t\t\t\t\ttmtransaksi trx\n" +
                "\t\tinner join tmreturn tr on tr.id_transaksi = trx.i_id\n" +
                "\t\twhere\n" +
                "\t\t\t\t\ttrx.d_pgun_rekam::date between :tanggalAwal and :tanggalAkhir\n" +
                "\t\t\tand trx.id_store = :idStore\n" +
                "\t\tgroup by\n" +
                "\t\t\ttrx.d_pgun_rekam::date, tr.total_return\n" +
                ") AS rekapSalesCashier\n" +
                "GROUP BY tanggal\n" +
                "ORDER BY tanggal";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idStore", idStore);
        map.addValue("tanggalAwal", tanggalAwal);
        map.addValue("tanggalAkhir", tanggalAkhir);
        return jdbcTemplate.query(query, map, new BeanPropertyRowMapper<>(LaporanCashierDto.LaporanCashier.class));
    }

}
