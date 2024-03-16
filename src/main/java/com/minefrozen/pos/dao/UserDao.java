package com.minefrozen.pos.dao;

import com.minefrozen.pos.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public Optional<UserDto.UserLogin> findByUsername(String username){
        String query = "select\n" +
                "\tid,\n" +
                "\tusername,\n" +
                "\t\"password\",\n" +
                "\t\"role\",\n" +
                "\tuser_store as userStore\n" +
                "from\n" +
                "\ttruser\n" +
                "where\n" +
                "\tusername = :username";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("username", username);
        try {
            UserDto.UserLogin data = jdbcTemplate.queryForObject(query, map, new BeanPropertyRowMapper<>(UserDto.UserLogin.class));
            if(data != null){
                return Optional.of(data);
            }else{
                return Optional.empty();
            }
        } catch(DataAccessException e){
            return Optional.empty();
        }
    }

}
