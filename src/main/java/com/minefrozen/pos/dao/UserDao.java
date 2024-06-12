package com.minefrozen.pos.dao;

import com.minefrozen.pos.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
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



    public List<UserDto.User> findAll(){
        String query = "select\n" +
                "\tid,\n" +
                "\tusername,\n" +
                "\t\"password\",\n" +
                "\t\"role\",\n" +
                "\ti_pgun_rekam as iPgunRekam,\n" +
                "\td_pgun_rekam as dPgunRekam,\n" +
                "\ti_pgun_ubah as iPgunUbah,\n" +
                "\td_pgun_ubah as dPgunUbah,\n" +
                "\tuser_store as userStore\n" +
                "from\n" +
                "\ttruser\n ";
        MapSqlParameterSource map = new MapSqlParameterSource();
        return jdbcTemplate.query(query, map, new BeanPropertyRowMapper<>(UserDto.User.class));
    }

    public Optional<UserDto.User> findById(Integer id){
        String query = "select\n" +
                "\tid,\n" +
                "\tusername,\n" +
                "\t\"password\",\n" +
                "\t\"role\",\n" +
                "\ti_pgun_rekam as iPgunRekam,\n" +
                "\td_pgun_rekam as dPgunRekam,\n" +
                "\ti_pgun_ubah as iPgunUbah,\n" +
                "\td_pgun_ubah as dPgunUbah,\n" +
                "\tuser_store as userStore\n" +
                "from\n" +
                "\ttruser\n" +
                "where id = :id\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id",id);
        try {
            UserDto.User data = jdbcTemplate.queryForObject(query, map, new BeanPropertyRowMapper<>(UserDto.User.class));
            if(data != null){
                return Optional.of(data);
            }else{
                return Optional.empty();
            }
        } catch(DataAccessException e){
            return Optional.empty();
        }
    }

    public void save(UserDto.User data){
        String query = "INSERT INTO truser\n" +
                "(id, username, \"password\", \"role\", i_pgun_rekam, d_pgun_rekam, user_store)\n" +
                "VALUES(:id, :username, :password, :role, :iPgunRekam, :dPgunRekam, :userStore)\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", data.getId());
        map.addValue("username", data.getUsername());
        map.addValue("password", data.getPassword());
        map.addValue("role", data.getRole());
        map.addValue("iPgunRekam", data.getIPgunRekam());
        map.addValue("dPgunRekam", data.getDPgunRekam());
        map.addValue("userStore", data.getUserStore());
        jdbcTemplate.update(query,map);
    }

    public void update(UserDto.User data){
        String query = "update\n" +
                "\ttruser\n" +
                "set\n" +
                "\tusername = :username,\n" +
                "\t\"password\" = :password,\n" +
                "\t\"role\" = :role,\n" +
                "\ti_pgun_ubah = :iPgunUbah,\n" +
                "\td_pgun_ubah = current_timestamp,\n" +
                "\tuser_store = :userStore\n" +
                "where id = :id\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", data.getId());
        map.addValue("username", data.getUsername());
        map.addValue("password", data.getPassword());
        map.addValue("role", data.getRole());
        map.addValue("iPgunUbah", data.getIPgunUbah());
        map.addValue("userStore", data.getUserStore());
        jdbcTemplate.update(query,map);
    }

    public void delete(Integer id){
        String query = "delete from truser where id = :id";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id",id);
        jdbcTemplate.update(query,map);
    }



}
