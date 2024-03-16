package com.minefrozen.pos.dao;

import com.minefrozen.pos.dto.ReturnDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class ReturnDao {

    @Autowired
    @Qualifier("posJdbc")
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("serverJdbc")
    private NamedParameterJdbcTemplate jdbcTemplateServer;

    public List<ReturnDto.Return> findAllReturn(Integer idStore){
        String query = "select\n" +
                "\ti_id as id,\n" +
                "\tid_store as idStore,\n" +
                "\tid_transaksi as idTransaksi,\n" +
                "\tid_produk as idProduk,\n" +
                "\texpired_date::date as expiredDate,\n" +
                "\tharga_jual as hargaJual,\n" +
                "\tdisc_produk as discProduk,\n" +
                "\tqty,\n" +
                "\ti_pgun_rekam as iPgunRekam,\n" +
                "\td_pgun_rekam as dPgunRekam,\n" +
                "\ttotal_return as totalReturn\n" +
                "from\n" +
                "\ttmreturn t\n" +
                "where\n" +
                "\tid_store = :idStore";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idStore",idStore);
        return jdbcTemplate.query(query, map, new BeanPropertyRowMapper<>(ReturnDto.Return.class));
    }

    public List<ReturnDto.FindTransaksiReturn> findTransaksiReturn(String kodeTransaksi, Integer idStore){
        String query = "select\n" +
                "\tt.i_id as idTransaksi,\n" +
                "\tt.id_store as idStore,\n" +
                "\tt.disc_member as discMember,\n" +
                "\ttrinci.id_produk as idProduk,\n" +
                "\ttrinci.expired_date as expiredDate,\n" +
                "\ttrinci.harga_jual as hargaJual,\n" +
                "\ttrinci.disc_produk as discProduk,\n" +
                "\ttrinci.qty as qty,\n" +
                "\tt.kode_transaksi as kodeTransaksi,\n" +
                "\tto_char(t.d_pgun_rekam, 'DD-MM-YYYY HH24:MI:SS') as tanggalTransaksi \n" +
                "from\n" +
                "\ttmtransaksi t\n" +
                "\tleft join tmtransaksirinci trinci on t.i_id = trinci.id_transaksi\n" +
                "where\n" +
                "\tkode_transaksi = :kodeTransaksi\n" +
                "\tand t.id_store = :idStore";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("kodeTransaksi",kodeTransaksi);
        map.addValue("idStore",idStore);
        return jdbcTemplate.query(query, map, new BeanPropertyRowMapper<>(ReturnDto.FindTransaksiReturn.class));
    }

    public void saveReturn(ReturnDto.Return data){
        String query = "insert\n" +
                "\tinto tmreturn\n" +
                "(i_id,\n" +
                "\tid_transaksi,\n" +
                "\tid_produk,\n" +
                "\texpired_date,\n" +
                "\tharga_jual,\n" +
                "\tdisc_produk,\n" +
                "\tid_store,\n" +
                "\ti_pgun_rekam,\n" +
                "\td_pgun_rekam,\n" +
                "\ttotal_return,\n" +
                "\tqty)\n" +
                "values(\n" +
                ":id,\n" +
                ":idTransaksi,\n" +
                ":idProduk,\n" +
                ":expiredDate,\n" +
                ":hargaJual,\n" +
                ":discProduk,\n" +
                ":idStore,\n" +
                ":iPgunRekam,\n" +
                "CURRENT_TIMESTAMP,\n" +
                ":totalReturn,\n" +
                ":qty)\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", data.getId());
        map.addValue("idTransaksi", data.getIdTransaksi());
        map.addValue("idProduk", data.getIdProduk());
        map.addValue("expiredDate", data.getExpiredDate());
        map.addValue("hargaJual", data.getHargaJual());
        map.addValue("discProduk", data.getDiscProduk());
        map.addValue("idStore", data.getIdStore());
        map.addValue("iPgunRekam", data.getIPgunRekam());
        map.addValue("qty", data.getQty());
        map.addValue("totalReturn", data.getTotalReturn());
        jdbcTemplate.update(query,map);
    }

    // Check QTY Transaksi Rinci
    public Optional<Integer> findTransaksiRinciForCheckQty(Integer idProduk, Integer idTransaksi){
        String query = "select qty from tmtransaksirinci where id_transaksi = :idTransaksi and id_produk = :idProduk";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idProduk", idProduk);
        map.addValue("idTransaksi", idTransaksi);
        try {
            Integer data = jdbcTemplateServer.queryForObject(query, map, Integer.class);
            if(data != null){
                return Optional.of(data);
            }else{
                return Optional.empty();
            }
        } catch(DataAccessException e){
            return Optional.empty();
        }
    }

    public void deleteTransaksiRinci(Integer idTransaksi, Integer idProduk, Integer idStore){
        String query = "delete\n" +
                "from\n" +
                "\ttmtransaksirinci\n" +
                "where\n" +
                "\tid_transaksi = :idTransaksi\n" +
                "\tand id_produk = :idProduk\n" +
                "\tand id_store = :idStore";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idTransaksi",idTransaksi);
        map.addValue("idProduk",idProduk);
        map.addValue("idStore",idStore);
        jdbcTemplate.update(query,map);
    }

    public void updateQtyTransaksiRinci(Integer qtyReturn, BigDecimal totalReturn, Integer idTransaksi, Integer idProduk){
        String query = "update\n" +
                "\ttmtransaksirinci\n" +
                "set\n" +
                "\tqty = qty - :qtyReturn,\n" +
                "\ttotal_per_produk = total_per_produk - :totalReturn\n" +
                "where\n" +
                "\tid_transaksi = :idTransaksi\n" +
                "\tand id_produk = :idProduk";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("qtyReturn", qtyReturn);
        map.addValue("totalReturn", totalReturn);
        map.addValue("idTransaksi", idTransaksi);
        map.addValue("idProduk", idProduk);
        jdbcTemplate.update(query,map);
    }

    public void updateTotalHargaTransaksi(Integer idTransaksi, Integer idStore, BigDecimal totalReturn){
        String query = "update\n" +
                "\ttmtransaksi\n" +
                "set\n" +
                "\ttotal_harga = total_harga - :totalReturn\n" +
                "where\n" +
                "\ti_id = :idTransaksi\n" +
                "\tand id_store = :idStore";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idTransaksi", idTransaksi);
        map.addValue("idStore", idStore);
        map.addValue("totalReturn", totalReturn);
        jdbcTemplate.update(query,map);
    }

    // SERVER
    public Optional<Integer> findExistsTransaksiServer(Integer idTransaksi, Integer idStore){
        String query = "select\n" +
                "\t1\n" +
                "from\n" +
                "\ttmtransaksi t\n" +
                "\tleft join tmtransaksirinci trinci on t.i_id = trinci.id_transaksi\n" +
                "where\n" +
                "\ti_id = :idTransaksi\n" +
                "\tand t.id_store = :idStore";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idTransaksi", idTransaksi);
        map.addValue("idStore", idStore);
        try {
            Integer data = jdbcTemplateServer.queryForObject(query, map, Integer.class);
            if(data != null){
                return Optional.of(data);
            }else{
                return Optional.empty();
            }
        } catch(DataAccessException e){
            return Optional.empty();
        }
    }

    public void deleteTransaksiRinciServer(Integer idTransaksi, Integer idProduk, Integer idStore){
        String query = "delete\n" +
                "from\n" +
                "\ttmtransaksirinci\n" +
                "where\n" +
                "\tid_transaksi = :idTransaksi\n" +
                "\tand id_produk = :idProduk\n" +
                "\tand id_store = :idStore";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idTransaksi",idTransaksi);
        map.addValue("idProduk",idProduk);
        map.addValue("idStore",idStore);
        jdbcTemplateServer.update(query,map);
    }

    public void updateQtyTransaksiRinciServer(Integer qtyReturn, BigDecimal totalReturn, Integer idTransaksi, Integer idProduk){
        String query = "update\n" +
                "\ttmtransaksirinci\n" +
                "set\n" +
                "\tqty = qty - :qtyReturn,\n" +
                "\ttotal_per_produk = total_per_produk - :totalReturn\n" +
                "where\n" +
                "\tid_transaksi = :idTransaksi\n" +
                "\tand id_produk = :idProduk";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("qtyReturn", qtyReturn);
        map.addValue("totalReturn", totalReturn);
        map.addValue("idTransaksi", idTransaksi);
        map.addValue("idProduk", idProduk);
        jdbcTemplateServer.update(query,map);
    }

    public void updateTotalHargaTransaksiServer(Integer idTransaksi, Integer idStore, BigDecimal totalReturn){
        String query = "update\n" +
                "\ttmtransaksi\n" +
                "set\n" +
                "\ttotal_harga = total_harga - :totalReturn\n" +
                "where\n" +
                "\ti_id = :idTransaksi\n" +
                "\tand id_store = :idStore";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idTransaksi", idTransaksi);
        map.addValue("idStore", idStore);
        map.addValue("totalReturn", totalReturn);
        jdbcTemplateServer.update(query,map);
    }

    public void saveReturnServer(ReturnDto.Return data){
        String query = "insert\n" +
                "\tinto tmreturn\n" +
                "(i_id,\n" +
                "\tid_transaksi,\n" +
                "\tid_produk,\n" +
                "\texpired_date,\n" +
                "\tharga_jual,\n" +
                "\tdisc_produk,\n" +
                "\tid_store,\n" +
                "\ti_pgun_rekam,\n" +
                "\td_pgun_rekam,\n" +
                "\ttotal_return,\n" +
                "\tqty)\n" +
                "values(\n" +
                ":id,\n" +
                ":idTransaksi,\n" +
                ":idProduk,\n" +
                ":expiredDate,\n" +
                ":hargaJual,\n" +
                ":discProduk,\n" +
                ":idStore,\n" +
                ":iPgunRekam,\n" +
                "CURRENT_TIMESTAMP,\n" +
                ":totalReturn,\n" +
                ":qty)\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", data.getId());
        map.addValue("idTransaksi", data.getIdTransaksi());
        map.addValue("idProduk", data.getIdProduk());
        map.addValue("expiredDate", data.getExpiredDate());
        map.addValue("hargaJual", data.getHargaJual());
        map.addValue("discProduk", data.getDiscProduk());
        map.addValue("idStore", data.getIdStore());
        map.addValue("iPgunRekam", data.getIPgunRekam());
        map.addValue("qty", data.getQty());
        map.addValue("totalReturn", data.getTotalReturn());
        jdbcTemplateServer.update(query,map);
    }

    public Optional<Integer> findProdukByIdAndExpired(Integer idProduk, Date expiredDate, Integer idStore){
        String query = "select i_id from tminventory where id_produk = :idProduk and expired_date::date = :expiredDate and id_store = :idStore";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idProduk", idProduk);
        map.addValue("idStore", idStore);
        map.addValue("expiredDate", expiredDate);
        try {
            Integer data = jdbcTemplateServer.queryForObject(query, map, Integer.class);
            if(data != null){
                return Optional.of(data);
            }else{
                return Optional.empty();
            }
        } catch(DataAccessException e){
            return Optional.empty();
        }
    }

    public void updateQtyInven(Integer id, Integer newQty){
        String query = "update tminventory set qty = (select qty + :newQty from tminventory where i_id = :id) where i_id = :id";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("newQty", newQty);
        map.addValue("id", id);
        jdbcTemplateServer.update(query,map);
    }

    public void saveInventory(ReturnDto.Inventory data){
        String query = "INSERT INTO tminventory\n" +
                "(i_id, id_produk, id_store, qty, expired_date, i_pgun_rekam, d_pgun_rekam)\n" +
                "VALUES(:id, :idProduk, :idStore, :qty, :expiredDate, :iPgunRekam, CURRENT_TIMESTAMP)\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", data.getId());
        map.addValue("idProduk", data.getIdProduk());
        map.addValue("idStore", data.getIdStore());
        map.addValue("qty", data.getQty());
        map.addValue("expiredDate", data.getExpiredDate());
        map.addValue("iPgunRekam", data.getIPgunRekam());
        jdbcTemplateServer.update(query, map);
    }

}
