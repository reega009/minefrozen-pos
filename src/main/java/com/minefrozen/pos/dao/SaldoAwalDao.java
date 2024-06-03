package com.minefrozen.pos.dao;

import com.minefrozen.pos.dto.MemberDto;
import com.minefrozen.pos.dto.SaldoAwalDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public class SaldoAwalDao {

    @Autowired
    @Qualifier("posJdbc")
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("serverJdbc")
    private NamedParameterJdbcTemplate jdbcTemplateServer;

    public Optional<SaldoAwalDto.SaldoAwal> findByTanggalSekarang(Integer nomorKasir, Integer idStore, Date tanggalSekarang){
        String query = "select\n" +
                "\tt.i_id as id,\n" +
                "\tt.modal_awal as modalAwal,\n" +
                "\tt.nomor_kasir as nomorKasir,\n" +
                "\tt.id_store as idStore,\n" +
                "\tt.i_pgun_rekam as iPgunRekam,\n" +
                "\tt.d_pgun_rekam as dPgunRekam\n" +
                "from\n" +
                "\ttmsaldoawal t \n" +
                "where t.d_pgun_rekam::date = :tanggalSekarang\n" +
                "and nomor_kasir = :nomorKasir\n" +
                "and id_store = :idStore";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("nomorKasir",nomorKasir);
        map.addValue("idStore",idStore);
        map.addValue("tanggalSekarang",tanggalSekarang);
        try {
            SaldoAwalDto.SaldoAwal data = jdbcTemplate.queryForObject(query, map, new BeanPropertyRowMapper<>(SaldoAwalDto.SaldoAwal.class));
            if(data != null){
                return Optional.of(data);
            }else{
                return Optional.empty();
            }
        } catch(DataAccessException e){
            return Optional.empty();
        }
    }


    public void save(SaldoAwalDto.SaldoAwal data){
        String query = "insert\n" +
                "\tinto\n" +
                "\ttmsaldoawal\n" +
                "(i_id,\n" +
                "\tmodal_awal,\n" +
                "\tnomor_kasir,\n" +
                "\tid_store,\n" +
                "\ti_pgun_rekam,\n" +
                "\td_pgun_rekam)\n" +
                "values(:id,\n" +
                ":modalAwal,\n" +
                ":nomorKasir,\n" +
                ":idStore,\n" +
                ":iPgunRekam,\n" +
                "current_timestamp);\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", data.getId());
        map.addValue("modalAwal", data.getModalAwal());
        map.addValue("nomorKasir", data.getNomorKasir());
        map.addValue("idStore", data.getIdStore());
        map.addValue("iPgunRekam", data.getIPgunRekam());
        jdbcTemplate.update(query,map);
    }

    public void update(SaldoAwalDto.SaldoAwal data){
        String query = "update\n" +
                "\ttmsaldoawal\n" +
                "set\n" +
                "\tmodal_awal =:modalAwal,\n" +
                "\ti_pgun_rekam =:iPgunRekam,\n" +
                "\td_pgun_rekam = current_timestamp\n" +
                "where\n" +
                "\ti_id = :id\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", data.getId());
        map.addValue("modalAwal", data.getModalAwal());
        map.addValue("iPgunRekam", data.getIPgunRekam());
        jdbcTemplate.update(query,map);
    }

    public void saveServer(SaldoAwalDto.SaldoAwal data){
        String query = "insert\n" +
                "\tinto\n" +
                "\ttmsaldoawal\n" +
                "(i_id,\n" +
                "\tmodal_awal,\n" +
                "\tnomor_kasir,\n" +
                "\tid_store,\n" +
                "\ti_pgun_rekam,\n" +
                "\td_pgun_rekam)\n" +
                "values(:id,\n" +
                ":modalAwal,\n" +
                ":nomorKasir,\n" +
                ":idStore,\n" +
                ":iPgunRekam,\n" +
                "current_timestamp);\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", data.getId());
        map.addValue("modalAwal", data.getModalAwal());
        map.addValue("nomorKasir", data.getNomorKasir());
        map.addValue("idStore", data.getIdStore());
        map.addValue("iPgunRekam", data.getIPgunRekam());
        jdbcTemplateServer.update(query,map);
    }

    public void updateServer(SaldoAwalDto.SaldoAwal data){
        String query = "update\n" +
                "\ttmsaldoawal\n" +
                "set\n" +
                "\tmodal_awal =:modalAwal,\n" +
                "\ti_pgun_rekam =:iPgunRekam,\n" +
                "\td_pgun_rekam = current_timestamp\n" +
                "where\n" +
                "\ti_id = :id\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", data.getId());
        map.addValue("modalAwal", data.getModalAwal());
        map.addValue("iPgunRekam", data.getIPgunRekam());
        jdbcTemplateServer.update(query,map);
    }

}
