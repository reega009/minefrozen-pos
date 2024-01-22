package com.minefrozen.pos.dao;

import com.minefrozen.pos.dto.TransaksiDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TransaksiDao {

    @Autowired
    @Qualifier("posJdbc")
    private NamedParameterJdbcTemplate jdbcTemplate;

    public void tambahTransaksi(TransaksiDto.TambahTransaksi tambahTransaksi){
        String query = "INSERT INTO tmtransaksi\n" +
                "(i_id, id_store, kode_transaksi, jenis_pembayaran, nomor_kasir, i_pgun_rekam, d_pgun_rekam)\n" +
                "VALUES(:id, :idStore, :kodeTransaksi, :jenisPembayaran, :nomorKasir, :iPgunRekam, CURRENT_TIMESTAMP)\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", tambahTransaksi.getId());
        map.addValue("idStore", tambahTransaksi.getIdStore());
        map.addValue("kodeTransaksi", tambahTransaksi.getKodeTransaksi());
        map.addValue("jenisPembayaran", tambahTransaksi.getJenisPembayaran());
        map.addValue("nomorKasir", tambahTransaksi.getNomorKasir());
        map.addValue("iPgunRekam", 1);
        jdbcTemplate.update(query, map);
    }

    public void tambahTransaksiRinci(TransaksiDto.TambahTransaksiRinci rinci){
        String query = "INSERT INTO tmtransaksirinci\n" +
                "(id_transaksi, id_produk, expired_date, qty)\n" +
                "VALUES(:idTransaksi, :idProduk, :expiredDate, :qty)\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idTransaksi", rinci.getIdTransaksi());
        map.addValue("idProduk", rinci.getIdProduk());
        map.addValue("expiredDate", rinci.getExpiredDate());
        map.addValue("qty", rinci.getQty());
        jdbcTemplate.update(query, map);
    }

}
