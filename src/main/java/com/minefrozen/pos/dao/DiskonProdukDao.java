package com.minefrozen.pos.dao;

import com.minefrozen.pos.dto.DiskonProdukDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DiskonProdukDao {

    @Autowired
    @Qualifier("posJdbc")
    private NamedParameterJdbcTemplate jdbcTemplate;


    public List<DiskonProdukDto.DiskonProduk> findAll(Integer idStore){
        String query = "select\n" +
                "\tid,\n" +
                "\tid_store as idStore,\n" +
                "\tjenis_diskon as jenisDiskon,\n" +
                "\tid_produk as idProduk,\n" +
                "\texpired_date_produk as expiredDateProduk,\n" +
                "\tdisc,\n" +
                "\ti_pgun_rekam as iPgunRekam,\n" +
                "\td_pgun_rekam as dPgunRekam,\n" +
                "\ti_pgun_ubah as iPgunUbah,\n" +
                "\td_pgun_ubah as dPgunUbah,\n" +
                "\ttanggal_awal_periode as tanggalAwalPeriode,\n" +
                "\ttanggal_akhir_periode as tanggalAkhirPeriode,\n" +
                "\tid_produk_bonus as idProdukBonus,\n" +
                "\tsyarat_qty_bonus as minQtyToGetBonus\n" +
                "from\n" +
                "\ttmdiskonproduk\n" +
                "where id_store = :idStore\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idStore", idStore);
        return jdbcTemplate.query(query, map, new BeanPropertyRowMapper<>(DiskonProdukDto.DiskonProduk.class));
    }

    public Optional<DiskonProdukDto.DiskonProduk> findById(Integer id, Integer idStore){
        String query = "select\n" +
                "\tid,\n" +
                "\tid_store as idStore,\n" +
                "\tjenis_diskon as jenisDiskon,\n" +
                "\tid_produk as idProduk,\n" +
                "\texpired_date_produk as expiredDateProduk,\n" +
                "\tdisc,\n" +
                "\ti_pgun_rekam as iPgunRekam,\n" +
                "\td_pgun_rekam as dPgunRekam,\n" +
                "\ti_pgun_ubah as iPgunUbah,\n" +
                "\td_pgun_ubah as dPgunUbah,\n" +
                "\ttanggal_awal_periode as tanggalAwalPeriode,\n" +
                "\ttanggal_akhir_periode as tanggalAkhirPeriode,\n" +
                "\tid_produk_bonus as idProdukBonus,\n" +
                "\tsyarat_qty_bonus as minQtyToGetBonus\n" +
                "from\n" +
                "\ttmdiskonproduk\n" +
                "where id_store = :idStore\n" +
                "and id = :id\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id",id);
        map.addValue("idStore",idStore);
        try {
            DiskonProdukDto.DiskonProduk data = jdbcTemplate.queryForObject(query, map, new BeanPropertyRowMapper<>(DiskonProdukDto.DiskonProduk.class));
            if(data != null){
                return Optional.of(data);
            }else{
                return Optional.empty();
            }
        } catch(DataAccessException e){
            return Optional.empty();
        }
    }

    public Optional<DiskonProdukDto.DiskonProduk> findCheckDisc(DiskonProdukDto.ParamCheckDiskon param){
        String query = "SELECT\n" +
                "    id,\n" +
                "    id_store AS idStore,\n" +
                "    jenis_diskon AS jenisDiskon,\n" +
                "    id_produk AS idProduk,\n" +
                "    expired_date_produk AS expiredDateProduk,\n" +
                "    COALESCE(disc, 0) as discProduk,\n" +
                "    i_pgun_rekam AS iPgunRekam,\n" +
                "    d_pgun_rekam AS dPgunRekam,\n" +
                "    i_pgun_ubah AS iPgunUbah,\n" +
                "    d_pgun_ubah AS dPgunUbah,\n" +
                "    tanggal_awal_periode AS tanggalAwalPeriode,\n" +
                "    tanggal_akhir_periode AS tanggalAkhirPeriode,\n" +
                "    COALESCE(id_produk_bonus, 0) AS idProdukBonus,\n" +
                "    COALESCE(syarat_qty_bonus, 0) AS minQtyToGetBonus\n" +
                "FROM\n" +
                "    tmdiskonproduk\n" +
                "WHERE\n" +
                "    id_store = :idStore\n" +
                "    AND id_produk = :id_produk \n" +
                "    AND expired_date_produk = :expiredDateProduk\n" +
                "    AND current_timestamp::date BETWEEN tanggal_awal_periode::date AND tanggal_akhir_periode::date\n" +
                "    AND (\n" +
                "        CASE\n" +
                "            WHEN jenis_diskon = 'BONUS_PRODUCT' THEN\n" +
                "                CASE\n" +
                "                    WHEN :qtyBeli >= syarat_qty_bonus THEN true\n" +
                "                    ELSE false\n" +
                "                END\n" +
                "            ELSE true\n" +
                "        END\n" +
                "    )\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idStore",param.getIdStore());
        map.addValue("idProduk",param.getIdProduk());
        map.addValue("qtyBeli",param.getQtyBeli());
        map.addValue("expiredDateProduk",param.getExpiredDateProduk());
        try {
            DiskonProdukDto.DiskonProduk data = jdbcTemplate.queryForObject(query, map, new BeanPropertyRowMapper<>(DiskonProdukDto.DiskonProduk.class));
            if(data != null){
                return Optional.of(data);
            }else{
                return Optional.empty();
            }
        } catch(DataAccessException e){
            return Optional.empty();
        }
    }

    public void save(DiskonProdukDto.DiskonProduk data){
        String query = "insert\n" +
                "\tinto\n" +
                "\tpublic.tmdiskonproduk\n" +
                "(id,\n" +
                "\tid_store,\n" +
                "\tjenis_diskon,\n" +
                "\tid_produk,\n" +
                "\texpired_date_produk,\n" +
                "\tdisc,\n" +
                "\ti_pgun_rekam,\n" +
                "\td_pgun_rekam,\n" +
                "\ttanggal_awal_periode,\n" +
                "\ttanggal_akhir_periode,\n" +
                "\tid_produk_bonus,\n" +
                "\tsyarat_qty_bonus)\n" +
                "values(:id,\n" +
                ":idStore,\n" +
                ":jenisDiskon,\n" +
                ":idProduk,\n" +
                ":expiredDateProduk,\n" +
                ":disc,\n" +
                ":iPgunRekam,\n" +
                "CURRENT_TIMESTAMP,\n" +
                ":tanggalAwalPeriode,\n" +
                ":tanggalAkhirPeriode,\n" +
                ":idProdukBonus,\n" +
                ":syaratQtyBonus)\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", data.getId());
        map.addValue("idStore", data.getIdStore());
        map.addValue("jenisDiskon", data.getJenisDiskon());
        map.addValue("idProduk", data.getIdProduk());
        map.addValue("expiredDateProduk", data.getExpiredDateProduk());
        map.addValue("disc", data.getDiscProduk());
        map.addValue("iPgunRekam", 1);
        map.addValue("tanggalAwalPeriode", data.getTanggalAwalPeriode());
        map.addValue("tanggalAkhirPeriode", data.getTanggalAkhirPeriode());
        map.addValue("idProdukBonus", data.getIdProdukBonus());
        map.addValue("syaratQtyBonus", data.getMinQtyToGetBonus());
        jdbcTemplate.update(query,map);
    }

    public void update(DiskonProdukDto.DiskonProduk data){
        String query = "update\n" +
                "\ttmdiskonproduk\n" +
                "set\n" +
                "\tjenis_diskon = :jenisDiskon,\n" +
                "\tid_produk = :idProduk,\n" +
                "\texpired_date_produk = :expiredDateProduk,\n" +
                "\tdisc = :disc,\n" +
                "\ti_pgun_ubah = :iPgunUbah,\n" +
                "\td_pgun_ubah = CURRENT_TIMESTAMP,\n" +
                "\ttanggal_awal_periode = :tanggalAwalPeriode,\n" +
                "\ttanggal_akhir_periode = :tanggalAkhirPeriode,\n" +
                "\tid_produk_bonus = :idProdukBonus,\n" +
                "\tsyarat_qty_bonus = :syaratQtyBonus\n" +
                "where id = :id\n" +
                "and id_store = :idStore\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", data.getId());
        map.addValue("idStore", data.getIdStore());
        map.addValue("jenisDiskon", data.getJenisDiskon());
        map.addValue("idProduk", data.getIdProduk());
        map.addValue("expiredDateProduk", data.getExpiredDateProduk());
        map.addValue("disc", data.getDiscProduk());
        map.addValue("iPgunUbah", 1);
        map.addValue("tanggalAwalPeriode", data.getTanggalAwalPeriode());
        map.addValue("tanggalAkhirPeriode", data.getTanggalAkhirPeriode());
        map.addValue("idProdukBonus", data.getIdProdukBonus());
        map.addValue("syaratQtyBonus", data.getMinQtyToGetBonus());
        jdbcTemplate.update(query,map);
    }

    public void delete(Integer id){
        String query = "delete from tmdiskonproduk where i_id = :id";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id",id);
        jdbcTemplate.update(query,map);
    }


}
