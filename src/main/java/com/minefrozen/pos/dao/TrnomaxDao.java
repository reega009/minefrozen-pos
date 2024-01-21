package com.minefrozen.pos.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TrnomaxDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public Integer findNoMax(String namaTabel){
        String query = "select id_terbaru from trnomax where nama_tabel = :namaTabel";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("namaTabel",namaTabel);
        return jdbcTemplate.queryForObject(query, map, Integer.class);
    }

    public void updateTrNomax(String namaTabel){
        String query = "update trnomax set id_terbaru = id_terbaru + 1 where nama_tabel = :namaTabel";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("namaTabel",namaTabel);
        jdbcTemplate.update(query, map);
    }

}
