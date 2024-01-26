package com.minefrozen.pos.dao;

import com.minefrozen.pos.dto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;


    public List<MemberDto.Member> findAll(Integer idStore){
        String query = "select\n" +
                "\tid,\n" +
                "\tid_store as idStore,\n" +
                "\tkode_member as kodeMember,\n" +
                "\tnama_member as namaMember,\n" +
                "\tnomor_handphone as nomorHandphone,\n" +
                "\taktif,\n" +
                "\tdisc_member as discMember,\n" +
                "\ti_pgun_rekam as iPgunRekam,\n" +
                "\td_pgun_rekam as dPgunRekam,\n" +
                "\ti_pgun_ubah as iPgunUbah,\n" +
                "\td_pgun_ubah as dPgunUbah\n" +
                "from\n" +
                "\ttmmember\n" +
                "where id_store = :idStore\n" +
                "and aktif = true\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idStore", idStore);
        return jdbcTemplate.query(query, map, new BeanPropertyRowMapper<>(MemberDto.Member.class));
    }

    public Optional<MemberDto.Member> findById(Integer id, Integer idStore){
        String query = "select\n" +
                "\tid,\n" +
                "\tid_store as idStore,\n" +
                "\tkode_member as kodeMember,\n" +
                "\tnama_member as namaMember,\n" +
                "\tnomor_handphone as nomorHandphone,\n" +
                "\taktif,\n" +
                "\tdisc_member as discMember,\n" +
                "\ti_pgun_rekam as iPgunRekam,\n" +
                "\td_pgun_rekam as dPgunRekam,\n" +
                "\ti_pgun_ubah as iPgunUbah,\n" +
                "\td_pgun_ubah as dPgunUbah\n" +
                "from\n" +
                "\ttmmember\n" +
                "where id_store = :idStore\n" +
                "and aktif = true\n" +
                "and id = :id\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id",id);
        map.addValue("idStore",idStore);
        try {
            MemberDto.Member data = jdbcTemplate.queryForObject(query, map, new BeanPropertyRowMapper<>(MemberDto.Member.class));
            if(data != null){
                return Optional.of(data);
            }else{
                return Optional.empty();
            }
        } catch(DataAccessException e){
            return Optional.empty();
        }
    }

    public Optional<MemberDto.Member> findByNameOrKodeMember(Integer idStore, String namaMember, String kodeMember){
        String baseQuery = "select\n" +
                "\tid,\n" +
                "\tid_store as idStore,\n" +
                "\tkode_member as kodeMember,\n" +
                "\tnama_member as namaMember,\n" +
                "\tnomor_handphone as nomorHandphone,\n" +
                "\taktif,\n" +
                "\tdisc_member as discMember,\n" +
                "\ti_pgun_rekam as iPgunRekam,\n" +
                "\td_pgun_rekam as dPgunRekam,\n" +
                "\ti_pgun_ubah as iPgunUbah,\n" +
                "\td_pgun_ubah as dPgunUbah\n" +
                "from\n" +
                "\ttmmember\n" +
                "where id_store = :idStore\n" +
                "and aktif = true";
        StringBuilder query = new StringBuilder(baseQuery);

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idStore",idStore);

        if(namaMember != null){
            query.append(" and lower(nama_member) like lower(:namaMember)");
            map.addValue("namaMember", new StringBuilder("%")
                    .append(namaMember).append("%").toString());
        }

        if(kodeMember != null){
            query.append(" and lower(kode_member) like lower(:kodeMember)");
            map.addValue("kodeMember", new StringBuilder("%")
                    .append(kodeMember).append("%").toString());
        }

        try {
            MemberDto.Member data = jdbcTemplate.queryForObject(query.toString(), map, new BeanPropertyRowMapper<>(MemberDto.Member.class));
            if(data != null){
                return Optional.of(data);
            }else{
                return Optional.empty();
            }
        } catch(DataAccessException e){
            return Optional.empty();
        }
    }

    public void save(MemberDto.Member data){
        String query = "insert into tmmember\n" +
                "(id,\n" +
                "\tid_store,\n" +
                "\tkode_member,\n" +
                "\tnama_member,\n" +
                "\tnomor_handphone,\n" +
                "\taktif,\n" +
                "\tdisc_member,\n" +
                "\ti_pgun_rekam,\n" +
                "\td_pgun_rekam)\n" +
                "values(:id,\n" +
                ":idStore,\n" +
                ":kodeMember,\n" +
                ":namaMember,\n" +
                ":nomorHandphone,\n" +
                "true,\n" +
                ":discMember,\n" +
                ":iPgunRekam,\n" +
                "CURRENT_TIMESTAMP)\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", data.getId());
        map.addValue("idStore", data.getIdStore());
        map.addValue("kodeMember", data.getKodeMember());
        map.addValue("namaMember", data.getNamaMember());
        map.addValue("nomorHandphone", data.getNomorHandphone());
        map.addValue("discMember", data.getDiscMember());
        map.addValue("iPgunRekam", 1);
        jdbcTemplate.update(query,map);
    }

    public void update(MemberDto.Member data){
        String query = "update\n" +
                "\ttmmember\n" +
                "set\n" +
                "\tid_store =:idStore,\n" +
                "\tkode_member = :kodeMember,\n" +
                "\tnama_member = :namaMember,\n" +
                "\tnomor_handphone = :nomorHandphone,\n" +
                "\tdisc_member = :discMember,\n" +
                "\ti_pgun_ubah = :iPgunUbah,\n" +
                "\td_pgun_ubah = CURRENT_TIMESTAMP";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idStore", data.getIdStore());
        map.addValue("kodeMember", data.getKodeMember());
        map.addValue("namaMember", data.getNamaMember());
        map.addValue("nomorHandphone", data.getNomorHandphone());
        map.addValue("discMember", data.getDiscMember());
        map.addValue("iPgunUbah", 1);
        jdbcTemplate.update(query,map);
    }

    public void delete(Integer id){
        String query = "delete from tmmember where i_id = :id";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id",id);
        jdbcTemplate.update(query,map);
    }


}
