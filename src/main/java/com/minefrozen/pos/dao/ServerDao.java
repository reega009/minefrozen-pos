package com.minefrozen.pos.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ServerDao {

    @Autowired
    @Qualifier("serverJdbc")
    private NamedParameterJdbcTemplate jdbcTemplate;


}
