package com.minefrozen.pos.dao;

import com.minefrozen.pos.dto.ProdukDto;
import com.minefrozen.pos.dto.TransaksiDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class TransaksiDao {

    @Autowired
    @Qualifier("posJdbc")
    private NamedParameterJdbcTemplate jdbcTemplate;

    public void tambahTransaksi(TransaksiDto.TambahTransaksi tambahTransaksi){
        String query = "INSERT INTO tmtransaksi\n" +
                "(i_id, id_store, kode_transaksi, jenis_pembayaran, nomor_kasir, shift, total_harga , id_member , disc_member , tanggal_tenggat_piutang, nomor_kartu_credit, jenis_debit, nama_kasir, i_pgun_rekam, d_pgun_rekam)\n" +
                "VALUES(:id, :idStore, :kodeTransaksi, :jenisPembayaran, :nomorKasir, :shift, :totalHarga, :idMember, :discMember, :tanggalTenggatPiutang, :nomorKartuCredit, :jenisDebit, :namaKasir , :iPgunRekam, :dPgunRekam)\n";
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
        map.addValue("namaKasir", tambahTransaksi.getNamaKasir());
        map.addValue("iPgunRekam", tambahTransaksi.getIPgunRekam());
        map.addValue("dPgunRekam", tambahTransaksi.getDPgunRekam());
        jdbcTemplate.update(query, map);
    }

    public void tambahTransaksiRinci(TransaksiDto.TambahTransaksiRinci rinci){
        String query = "INSERT INTO tmtransaksirinci\n" +
                "(id_transaksi, id_produk, id_store, qty, harga_jual, harga_beli, disc_produk, total_per_produk)\n" +
                "VALUES(:idTransaksi, :idProduk, :idStore, :qty, :hargaJual, :hargaBeli, :discProduk, :totalHargaPerProduk)\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idTransaksi", rinci.getIdTransaksi());
        map.addValue("idProduk", rinci.getIdProduk());
        map.addValue("idStore", rinci.getIdStore());
        map.addValue("qty", rinci.getQty());
        map.addValue("hargaJual", rinci.getHargaJual());
        map.addValue("hargaBeli", rinci.getHargaBeli());
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

    public Optional<Integer> checkIfTrxExist(Integer idStore, Integer nomorKasir, Timestamp tanggalTransaksi, Integer jenisPembayaran, String namaKasir, BigDecimal totalHarga){
        String query = "SELECT DISTINCT\n" +
                "    1\n" +
                "FROM\n" +
                "    tmtransaksi t\n" +
                "WHERE\n" +
                "    id_store = :idStore\n" +
                "    AND nomor_kasir = :nomorKasir\n" +
                "    AND d_pgun_rekam BETWEEN (CAST(:tanggalTransaksi AS TIMESTAMP) - INTERVAL '1 minute') AND CAST(:tanggalTransaksi AS TIMESTAMP)\n" +
                "    AND jenis_pembayaran = :jenisPembayaran\n" +
                "    AND nama_kasir = :namaKasir\n" +
                "    AND total_harga = :totalHarga;";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idStore", idStore);
        map.addValue("nomorKasir", nomorKasir);
        map.addValue("tanggalTransaksi", tanggalTransaksi);
        map.addValue("jenisPembayaran", jenisPembayaran);
        map.addValue("namaKasir", namaKasir);
        map.addValue("totalHarga", totalHarga);
        try {
            Integer data = jdbcTemplate.queryForObject(query, map, Integer.class);
            return Optional.ofNullable(data);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public List<TransaksiDto.Transaksi> findAll(Integer idStore, Integer nomorKasir, Integer shift, Date tanggalAwal, Date tanggalAkhir){
        String query = "select\n" +
                "\tnoUrut,\n" +
                "\tid,\n" +
                "\tkodeTransaksi,\n" +
                "\ttanggalTransaksi,\n" +
                "\tnamaPembayaran,\n" +
                "\tnomorKasir,\n" +
                "\tshift,\n" +
                "\tnamaUser,\n" +
                "\tdiscMember,\n" +
                "\ttotalHargaPerTransaksi\n" +
                "from(\n" +
                "\tselect\n" +
                "\t\t1 as noUrut,\n" +
                "\t\ttrx.i_id as id,\n" +
                "\t\tcase\n" +
                "\t\t\twhen trx.jenis_pembayaran = 1 then 'Cash'\n" +
                "\t\t\twhen trx.jenis_pembayaran = 2 then \n" +
                "\t\t\t\tcase \n" +
                "\t\t\t\t\twhen trx.jenis_debit = 1 then 'Debit BCA'\n" +
                "\t\t\t\t\twhen trx.jenis_debit = 2 then 'Debit Mandiri'\n" +
                "\t\t\t\tend\n" +
                "\t\t\twhen trx.jenis_pembayaran = 3 then \n" +
                "\t\t\t\tcase \n" +
                "\t\t\t\t\twhen trx.jenis_debit = 1 then 'Credit BCA'\n" +
                "\t\t\t\t\twhen trx.jenis_debit = 2 then 'Credit Mandiri'\n" +
                "\t\t\t\tend\n" +
                "\t\t\twhen trx.jenis_pembayaran = 4 then 'Piutang'\n" +
                "\t\t\twhen trx.jenis_pembayaran = 5 then \n" +
                "\t\t\t\tcase \n" +
                "\t\t\t\t\twhen trx.jenis_debit = 1 then 'QRIS BCA'\n" +
                "\t\t\t\t\twhen trx.jenis_debit = 2 then 'QRIS Mandiri'\n" +
                "\t\t\t\tend\n" +
                "\t\tend as namaPembayaran,\n" +
                "\t\tconcat('Kasir ',trx.nomor_kasir) as nomorKasir,\n" +
                "\t\tconcat('Shift ',trx.shift) as shift,\n" +
                "\t\ttrx.kode_transaksi as kodeTransaksi,\n" +
                "\t\ttrx.total_harga as totalHargaPerTransaksi,\n" +
                "\t\ttrx.disc_member as discMember,\n" +
                "\t\ttuser.username as namaUser,\n" +
                "\t\tTO_CHAR(trx.d_pgun_rekam, 'DD-MM-YYYY HH24:MI:SS') as tanggalTransaksi\n" +
                "\tFROM \n" +
                "\t    tmtransaksi trx \n" +
                "\t    left join truser tuser on tuser.id = trx.i_pgun_rekam\n" +
                "\twhere trx.id_store = :idStore\n" +
                "\tand nomor_kasir = :nomorKasir\n" +
                "\tand shift = :shift\n" +
                "\tAND trx.d_pgun_rekam::date BETWEEN :tanggalAwal AND :tanggalAkhir\n" +
                "union all\n" +
                "\tselect distinct\n" +
                "\t\t2 as noUrut,\n" +
                "\t\t0 as id,\n" +
                "\t\t'' as namaPembayaran,\n" +
                "\t\t'' as nomorKasir,\n" +
                "\t\t'' as shift,\n" +
                "\t\t'TOTAL' as kodeTransaksi,\n" +
                "\t\tsum(trx.total_harga) - (sum(trx.total_harga) * sum(disc_member) / 100) as totalHargaPerTransaksi,\n" +
                "\t\t0 as discMember,\n" +
                "\t\t'' as namaUser,\n" +
                "\t\t'' as tanggalTransaksi\n" +
                "\tFROM \n" +
                "\t    tmtransaksi trx \n" +
                "\t    left join truser tuser on tuser.id = trx.i_pgun_rekam\n" +
                "\twhere trx.id_store = :idStore\n" +
                "\tand nomor_kasir = :nomorKasir\n" +
                "\tand shift = :shift\n" +
                "\tAND trx.d_pgun_rekam::date BETWEEN :tanggalAwal AND :tanggalAkhir\n" +
                "\t) as rekapTransaksi order by nourut asc, id desc";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idStore", idStore);
        map.addValue("nomorKasir", nomorKasir);
        map.addValue("shift", shift);
        map.addValue("tanggalAwal", tanggalAwal);
        map.addValue("tanggalAkhir", tanggalAkhir);
        return jdbcTemplate.query(query, map, new BeanPropertyRowMapper<>(TransaksiDto.Transaksi.class));
    }

    public List<TransaksiDto.TransaksiRinci> findAllRinci(Integer idStore, Integer idTransaksi){
        String query = "SELECT\n" +
                "\tcase\n" +
                "\t\t\twhen trx.jenis_pembayaran = 1 then 'Cash'\n" +
                "\t\t\twhen trx.jenis_pembayaran = 2 then \n" +
                "\t\t\t\tcase \n" +
                "\t\t\t\t\twhen trx.jenis_debit = 1 then 'Debit BCA'\n" +
                "\t\t\t\t\twhen trx.jenis_debit = 2 then 'Debit Mandiri'\n" +
                "\t\t\t\tend\n" +
                "\t\t\twhen trx.jenis_pembayaran = 3 then \n" +
                "\t\t\t\tcase \n" +
                "\t\t\t\t\twhen trx.jenis_debit = 1 then 'Credit BCA'\n" +
                "\t\t\t\t\twhen trx.jenis_debit = 2 then 'Credit Mandiri'\n" +
                "\t\t\t\tend\n" +
                "\t\t\twhen trx.jenis_pembayaran = 4 then 'Piutang'\n" +
                "\t\t\twhen trx.jenis_pembayaran = 5 then \n" +
                "\t\t\t\tcase \n" +
                "\t\t\t\t\twhen trx.jenis_debit = 1 then 'QRIS BCA'\n" +
                "\t\t\t\t\twhen trx.jenis_debit = 2 then 'QRIS Mandiri'\n" +
                "\t\t\t\tend\n" +
                "\tend as jenisPembayaran,\n" +
                "\ttprd.kode_product as kodeProduk,\n" +
                "\ttprd.nama_product as namaProduk,\n" +
                "\ttrxrinci.harga_jual as hargaJual,\n" +
                "\ttrxrinci.disc_produk as discProduk,\n" +
                "\ttrxrinci.qty,\n" +
                "\ttrxrinci.total_per_produk as totalHargaPerProduk,\n" +
                "\ttrx.kode_transaksi as kodeTransaksi,\n" +
                "\tTO_CHAR(trx.d_pgun_rekam, 'DD-MM-YYYY HH24:MI:SS') as tanggalTransaksi,\n" +
                "\ttrx.nomor_kasir as nomorKasir,\n" +
                "\ttuser.username as namaKasir,\n" +
                "\ttrx.disc_member as discMember,\n" +
                "\ttrx.total_harga as totalHarga,\n" +
                "\ttrx.nomor_kartu_credit as nomorKartuCredit\n" +
                "FROM \n" +
                "    tmtransaksi trx \n" +
                "    left join tmtransaksirinci trxrinci on trx.i_id = trxrinci.id_transaksi \n" +
                "    left join trproduct tprd on tprd.i_id = trxrinci.id_produk\n" +
                "    left join truser tuser on tuser.id = trx.i_pgun_rekam\n" +
                "where trx.id_store = :idStore\n" +
                "and trx.i_id = :idTransaksi";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idStore", idStore);
        map.addValue("idTransaksi", idTransaksi);
        return jdbcTemplate.query(query, map, new BeanPropertyRowMapper<>(TransaksiDto.TransaksiRinci.class));
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
        jdbcTemplate.update(query, map);
    }

}
