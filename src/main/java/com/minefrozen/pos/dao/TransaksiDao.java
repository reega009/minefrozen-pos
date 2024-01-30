package com.minefrozen.pos.dao;

import com.minefrozen.pos.dto.TransaksiDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
                "(i_id, id_store, kode_transaksi, jenis_pembayaran, nomor_kasir, total_harga , id_member , disc_member , i_pgun_rekam, d_pgun_rekam)\n" +
                "VALUES(:id, :idStore, :kodeTransaksi, :jenisPembayaran, :nomorKasir, :totalHarga, :idMember, :discMember , :iPgunRekam, :dPgunRekam)\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", tambahTransaksi.getId());
        map.addValue("idStore", tambahTransaksi.getIdStore());
        map.addValue("kodeTransaksi", tambahTransaksi.getKodeTransaksi());
        map.addValue("jenisPembayaran", tambahTransaksi.getJenisPembayaran());
        map.addValue("nomorKasir", tambahTransaksi.getNomorKasir());
        map.addValue("totalHarga", tambahTransaksi.getTotalHargaPerTransaksi());
        map.addValue("idMember", tambahTransaksi.getIdMember());
        map.addValue("discMember", tambahTransaksi.getDiscMember());
        map.addValue("iPgunRekam", 1);
        map.addValue("dPgunRekam", tambahTransaksi.getDPgunRekam());
        jdbcTemplate.update(query, map);
    }

    public void tambahTransaksiRinci(TransaksiDto.TambahTransaksiRinci rinci){
        String query = "INSERT INTO tmtransaksirinci\n" +
                "(id_transaksi, id_produk, id_store, expired_date, qty, harga_jual, disc_produk, total_per_produk)\n" +
                "VALUES(:idTransaksi, :idProduk, :idStore, :expiredDate, :qty, :hargaJual, :discProduk, :totalHargaPerProduk)\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idTransaksi", rinci.getIdTransaksi());
        map.addValue("idProduk", rinci.getIdProduk());
        map.addValue("idStore", rinci.getIdStore());
        map.addValue("expiredDate", rinci.getExpiredDate());
        map.addValue("qty", rinci.getQty());
        map.addValue("hargaJual", rinci.getHargaJual());
        map.addValue("discProduk", rinci.getDiscProduk());
        map.addValue("totalHargaPerProduk", rinci.getTotalHargaPerProduk());
        jdbcTemplate.update(query, map);
    }

    public Integer countTransaksiToday(Integer idStore){
        String query = "select count(i_id) + 1 from tmtransaksi t where id_store = :idStore and d_pgun_rekam::date = CURRENT_TIMESTAMP::date\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idStore", idStore);
        return jdbcTemplate.queryForObject(query, map, Integer.class);
    }

}
