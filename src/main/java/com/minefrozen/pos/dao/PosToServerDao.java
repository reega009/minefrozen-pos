package com.minefrozen.pos.dao;

import com.minefrozen.pos.dto.ServerDto;
import com.minefrozen.pos.dto.TransaksiDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public class PosToServerDao {

    @Autowired
    @Qualifier("posJdbc")
    private NamedParameterJdbcTemplate jdbcTemplatePos;

    @Autowired
    @Qualifier("serverJdbc")
    private NamedParameterJdbcTemplate jdbcTemplateServer;

    // ---------------- BACKUP -----------------
    public void tambahTransaksiBackup(TransaksiDto.TambahTransaksi tambahTransaksi){
        String query = "INSERT INTO tmtransaksibackup\n" +
                "(i_id, id_store, kode_transaksi, jenis_pembayaran, nomor_kasir, i_pgun_rekam, d_pgun_rekam)\n" +
                "VALUES(:id, :idStore, :kodeTransaksi, :jenisPembayaran, :nomorKasir, :iPgunRekam, :dPgunRekam)\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", tambahTransaksi.getId());
        map.addValue("idStore", tambahTransaksi.getIdStore());
        map.addValue("kodeTransaksi", tambahTransaksi.getKodeTransaksi());
        map.addValue("jenisPembayaran", tambahTransaksi.getJenisPembayaran());
        map.addValue("nomorKasir", tambahTransaksi.getNomorKasir());
        map.addValue("iPgunRekam", 1);
        map.addValue("dPgunRekam", tambahTransaksi.getDPgunRekam());
        jdbcTemplatePos.update(query, map);
    }

    public void tambahTransaksiRinciBackup(TransaksiDto.TambahTransaksiRinci rinci){
        String query = "INSERT INTO tmtransaksirincibackup\n" +
                "(id_transaksi, id_store, id_produk, expired_date, qty)\n" +
                "VALUES(:idTransaksi, :idStore, :idProduk, :expiredDate, :qty)\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idTransaksi", rinci.getIdTransaksi());
        map.addValue("idStore", rinci.getIdStore());
        map.addValue("idProduk", rinci.getIdProduk());
        map.addValue("expiredDate", rinci.getExpiredDate());
        map.addValue("qty", rinci.getQty());
        jdbcTemplatePos.update(query, map);
    }

    public List<TransaksiDto.TambahTransaksi> findAllTransaksiBackup(){
        String query = "SELECT i_id as id,\n" +
                "id_store as idStore,\n" +
                "kode_transaksi as kodeTransaksi,\n" +
                "i_pgun_rekam as iPgunRekam,\n" +
                "d_pgun_rekam as dPgunRekam,\n" +
                "i_pgun_ubah as iPgunUbah,\n" +
                "d_pgun_ubah as dPgunUbah,\n" +
                "jenis_pembayaran as jenisPembayaran,\n" +
                "nomor_kasir as nomorKasir\n" +
                "FROM tmtransaksibackup\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        return jdbcTemplatePos.query(query, map, new BeanPropertyRowMapper<>(TransaksiDto.TambahTransaksi.class));
    }

    public List<TransaksiDto.TambahTransaksiRinci> findAllTransaksiRinciBackup(Integer idTransaksi){
        String query = "SELECT id_transaksi as idTransaksi,\n" +
                "id_produk as idProduk,\n" +
                "id_store as idStore,\n" +
                "expired_date as expiredDate,\n" +
                "qty\n" +
                "FROM tmtransaksirincibackup\n" +
                "where id_transaksi = :idTransaksi\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idTransaksi", idTransaksi);
        return jdbcTemplatePos.query(query, map, new BeanPropertyRowMapper<>(TransaksiDto.TambahTransaksiRinci.class));
    }

    public void deleteDataBackup(){
        String query = "delete from tmtransaksibackup";
        MapSqlParameterSource map = new MapSqlParameterSource();
        jdbcTemplatePos.update(query, map);
    }

    public void deleteDataBackupRinci(){
        String query = "delete from tmtransaksirincibackup";
        MapSqlParameterSource map = new MapSqlParameterSource();
        jdbcTemplatePos.update(query, map);
    }

    // --------------- SERVER ---------------------

    public void tambahTransaksiServer(TransaksiDto.TambahTransaksi tambahTransaksi){
        String query = "INSERT INTO tmtransaksi\n" +
                "(i_id, id_store, kode_transaksi, jenis_pembayaran, nomor_kasir, i_pgun_rekam, d_pgun_rekam)\n" +
                "VALUES(:id, :idStore, :kodeTransaksi, :jenisPembayaran, :nomorKasir, :iPgunRekam, :dPgunRekam)\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", tambahTransaksi.getId());
        map.addValue("idStore", tambahTransaksi.getIdStore());
        map.addValue("kodeTransaksi", tambahTransaksi.getKodeTransaksi());
        map.addValue("jenisPembayaran", tambahTransaksi.getJenisPembayaran());
        map.addValue("nomorKasir", tambahTransaksi.getNomorKasir());
        map.addValue("iPgunRekam", 1);
        map.addValue("dPgunRekam", tambahTransaksi.getDPgunRekam());
        jdbcTemplateServer.update(query, map);
    }

    public void tambahTransaksiRinciServer(TransaksiDto.TambahTransaksiRinci rinci){
        String query = "INSERT INTO tmtransaksirinci\n" +
                "(id_transaksi, id_produk, id_store, expired_date, qty)\n" +
                "VALUES(:idTransaksi, :idProduk, :idStore, :expiredDate, :qty)\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idTransaksi", rinci.getIdTransaksi());
        map.addValue("idProduk", rinci.getIdProduk());
        map.addValue("idStore", rinci.getIdStore());
        map.addValue("expiredDate", rinci.getExpiredDate());
        map.addValue("qty", rinci.getQty());
        jdbcTemplateServer.update(query, map);
    }

    public void updateQtyInvenServer(Integer idStore, Integer idProduk, Date expiredDate, Integer qtyBeli){
        String query = "update \n" +
                "\ttminventory \n" +
                "set\n" +
                "\tqty = qty - :qtyBeli\n" +
                "where\n" +
                "\tid_store = :idStore\n" +
                "\tand id_produk = :idProduk\n" +
                "\tand expired_date = :expiredDate";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idStore", idStore);
        map.addValue("idProduk", idProduk);
        map.addValue("expiredDate", expiredDate);
        map.addValue("qtyBeli", qtyBeli);
        jdbcTemplateServer.update(query,map);
    }

    public Integer testServer(){
        String query = "select count(*) from tminventory t  ";
        MapSqlParameterSource map = new MapSqlParameterSource();
        return jdbcTemplateServer.queryForObject(query, map, Integer.class);
    }

}
