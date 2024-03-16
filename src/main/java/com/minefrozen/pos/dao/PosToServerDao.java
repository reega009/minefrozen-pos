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
                "(i_id, id_store, kode_transaksi, jenis_pembayaran, nomor_kasir, shift, total_harga , id_member , disc_member, tanggal_tenggat_piutang, nomor_kartu_credit, jenis_debit , i_pgun_rekam, d_pgun_rekam)\n" +
                "VALUES(:id, :idStore, :kodeTransaksi, :jenisPembayaran, :nomorKasir, :shift, :totalHarga, :idMember, :discMember, :tanggalTenggatPiutang, :nomorKartuCredit, :jenisDebit , :iPgunRekam, :dPgunRekam)";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", tambahTransaksi.getId());
        map.addValue("idStore", tambahTransaksi.getIdStore());
        map.addValue("kodeTransaksi", tambahTransaksi.getKodeTransaksi());
        map.addValue("jenisPembayaran", tambahTransaksi.getJenisPembayaran());
        map.addValue("nomorKasir", tambahTransaksi.getNomorKasir());
        map.addValue("shift", tambahTransaksi.getShift());
        map.addValue("totalHarga", tambahTransaksi.getTotalHargaPerTransaksi());
        map.addValue("idMember", tambahTransaksi.getIdMember());
        map.addValue("discMember", tambahTransaksi.getDiscMember());
        map.addValue("tanggalTenggatPiutang", tambahTransaksi.getTanggalTenggatPiutang());
        map.addValue("nomorKartuCredit", tambahTransaksi.getNomorKartuCredit());
        map.addValue("jenisDebit", tambahTransaksi.getJenisDebit());
        map.addValue("iPgunRekam", tambahTransaksi.getIPgunRekam());
        map.addValue("dPgunRekam", tambahTransaksi.getDPgunRekam());
        jdbcTemplatePos.update(query, map);
    }

    public void tambahTransaksiRinciBackup(TransaksiDto.TambahTransaksiRinci rinci){
        String query = "INSERT INTO tmtransaksirincibackup\n" +
                "(id_transaksi, id_produk, id_store, qty, harga_jual, disc_produk, total_per_produk)\n" +
                "VALUES(:idTransaksi, :idProduk, :idStore, :qty, :hargaJual, :discProduk, :totalHargaPerProduk)";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idTransaksi", rinci.getIdTransaksi());
        map.addValue("idStore", rinci.getIdStore());
        map.addValue("idProduk", rinci.getIdProduk());
        map.addValue("qty", rinci.getQty());
        map.addValue("hargaJual", rinci.getHargaJual());
        map.addValue("discProduk", rinci.getDiscProduk());
        map.addValue("totalHargaPerProduk", rinci.getTotalHargaPerProduk());
        jdbcTemplatePos.update(query, map);
    }

    public List<TransaksiDto.TambahTransaksi> findAllTransaksiBackup(){
        String query = "SELECT i_id as id,\n" +
                "id_store as idStore,\n" +
                "jenis_pembayaran as jenisPembayaran,\n" +
                "nomor_kasir as nomorKasir,\n" +
                "shift as shift,\n" +
                "kode_transaksi as kodeTransaksi,\n" +
                "i_pgun_rekam as iPgunRekam,\n" +
                "d_pgun_rekam as dPgunRekam,\n" +
                "total_harga as totalHargaPerTransaksi,\n" +
                "id_member as idMember,\n" +
                "disc_member as discMember,\n" +
                "tanggal_tenggat_piutang as tanggalTenggatPiutang,\n" +
                "nomor_kartu_credit as nomorKartuCredit,\n" +
                "jenis_debit as jenisDebit\n" +
                "FROM tmtransaksibackup\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        return jdbcTemplatePos.query(query, map, new BeanPropertyRowMapper<>(TransaksiDto.TambahTransaksi.class));
    }

    public List<TransaksiDto.TambahTransaksiRinci> findAllTransaksiRinciBackup(Integer idTransaksi){
        String query = "SELECT id_transaksi as idTransaksi,\n" +
                "id_produk as idProduk,\n" +
                "id_store as idStore,\n" +
                "qty,\n" +
                "harga_jual as hargaJual,\n" +
                "disc_produk as discProduk,\n" +
                "total_per_produk as totalHargaPerProduk\n" +
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
                "(i_id, id_store, kode_transaksi, jenis_pembayaran, nomor_kasir, shift, total_harga , id_member , disc_member, tanggal_tenggat_piutang, nomor_kartu_credit, jenis_debit , i_pgun_rekam, d_pgun_rekam)\n" +
                "VALUES(:id, :idStore, :kodeTransaksi, :jenisPembayaran, :nomorKasir, :shift, :totalHarga, :idMember, :discMember, :tanggalTenggatPiutang, :nomorKartuCredit, :jenisDebit , :iPgunRekam, :dPgunRekam)";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", tambahTransaksi.getId());
        map.addValue("idStore", tambahTransaksi.getIdStore());
        map.addValue("kodeTransaksi", tambahTransaksi.getKodeTransaksi());
        map.addValue("jenisPembayaran", tambahTransaksi.getJenisPembayaran());
        map.addValue("nomorKasir", tambahTransaksi.getNomorKasir());
        map.addValue("shift", tambahTransaksi.getShift());
        map.addValue("totalHarga", tambahTransaksi.getTotalHargaPerTransaksi());
        map.addValue("idMember", tambahTransaksi.getIdMember());
        map.addValue("discMember", tambahTransaksi.getDiscMember());
        map.addValue("tanggalTenggatPiutang", tambahTransaksi.getTanggalTenggatPiutang());
        map.addValue("nomorKartuCredit", tambahTransaksi.getNomorKartuCredit());
        map.addValue("jenisDebit", tambahTransaksi.getJenisDebit());
        map.addValue("iPgunRekam", tambahTransaksi.getIPgunRekam());
        map.addValue("dPgunRekam", tambahTransaksi.getDPgunRekam());
        jdbcTemplateServer.update(query, map);
    }

    public void tambahTransaksiRinciServer(TransaksiDto.TambahTransaksiRinci rinci){
        String query = "INSERT INTO tmtransaksirinci\n" +
                "(id_transaksi, id_produk, id_store, qty, harga_jual, disc_produk, total_per_produk)\n" +
                "VALUES(:idTransaksi, :idProduk, :idStore, :qty, :hargaJual, :discProduk, :totalHargaPerProduk)";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idTransaksi", rinci.getIdTransaksi());
        map.addValue("idProduk", rinci.getIdProduk());
        map.addValue("idStore", rinci.getIdStore());
        map.addValue("qty", rinci.getQty());
        map.addValue("hargaJual", rinci.getHargaJual());
        map.addValue("discProduk", rinci.getDiscProduk());
        map.addValue("totalHargaPerProduk", rinci.getTotalHargaPerProduk());
        jdbcTemplateServer.update(query, map);
    }


    public void tambahPiutang(ServerDto.TambahPiutang data){
        String query = "INSERT INTO tmpiutang\n" +
                "(i_id, id_transaksi, total_pelunasan, id_store)\n" +
                "VALUES(:id, :idTransaksi, :totalPelunasan, :idStore)";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", data.getId());
        map.addValue("idTransaksi", data.getIdTransaksi());
        map.addValue("totalPelunasan", data.getTotalPelunasan());
        map.addValue("idStore", data.getIdStore());
        jdbcTemplateServer.update(query,map);

    }

    // --------------- NEW SCHEMA FOR SUBSTRACT INVENTORY -----------------
    public List<TransaksiDto.ListSubstractInventory> listSubstractInventory(Integer idProduk, Integer idStore){
        String query = "select\n" +
                "\ti_id as idInventory,\n" +
                "\tqty,\n" +
                "\tid_produk as idProduk,\n" +
                "\texpired_date as expiredDate\n" +
                "from\n" +
                "\ttminventory t\n" +
                "where\n" +
                "\tid_produk = :idProduk\n" +
                "\tand id_store = :idStore\n" +
                "order by\n" +
                "\texpired_date asc";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idProduk", idProduk);
        map.addValue("idStore", idStore);
        return jdbcTemplateServer.query(query, map, new BeanPropertyRowMapper<>(TransaksiDto.ListSubstractInventory.class));
    }

    public void updateQtyInvenServer(Integer idStore, Integer idProduk, Date expiredDate, Integer qtyBeli){
        String query = "update \n" +
                "\ttminventory \n" +
                "set\n" +
                "\tqty = qty - (qty + :qtyBeli)\n" +
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

    public void deleteInvenServer(Integer idInventory, Integer idStore, Date expiredDate){
        String query = "delete\n" +
                "from\n" +
                "\ttminventory\n" +
                "where\n" +
                "\ti_id = :idInventory\n" +
                "\tand id_store = :idStore\n" +
                "\tand expired_date = :expiredDate";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idInventory", idInventory);
        map.addValue("idStore", idStore);
        map.addValue("expiredDate", expiredDate);
        jdbcTemplateServer.update(query,map);
    }

    public void changeTipePembayaran(TransaksiDto.ChangeTipePembayaran data){
        String query = "update\n" +
                "\ttmtransaksi\n" +
                "set\n" +
                "\tjenis_pembayaran = :jenisPembayaran,\n" +
                "\tjenis_debit = :jenisDebit,\n" +
                "\tnomor_kartu_credit = :nomorKartuCredit,\n" +
                "\ti_pgun_ubah = :iPgunUbah,\n" +
                "\td_pgun_ubah = current_timestamp\n" +
                "where\n" +
                "\ti_id = :idTransaksi\n" +
                "\tand id_store = :idStore";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("jenisPembayaran", data.getJenisPembayaran());
        map.addValue("jenisDebit", data.getJenisDebit());
        map.addValue("nomorKartuCredit", data.getNomorKartuCredit());
        map.addValue("iPgunUbah", -1);
        map.addValue("idTransaksi", data.getIdTransaksi());
        map.addValue("idStore", data.getIdStore());
        jdbcTemplateServer.update(query, map);
    }

    public Integer testServer(){
        String query = "select count(*) from tminventory t  ";
        MapSqlParameterSource map = new MapSqlParameterSource();
        return jdbcTemplateServer.queryForObject(query, map, Integer.class);
    }

}
