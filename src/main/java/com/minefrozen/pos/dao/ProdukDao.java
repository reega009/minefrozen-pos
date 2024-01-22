package com.minefrozen.pos.dao;

import com.minefrozen.pos.dto.ProdukDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public class ProdukDao {

    @Autowired
    @Qualifier("posJdbc")
    private NamedParameterJdbcTemplate jdbcTemplate;

    public Optional<ProdukDto.ProdukKasir> findProdukByBarcode(String barcode){
        String query = "select\n" +
                "\tt.i_id as id,\n" +
                "\tt.kode_product as kodeProduct,\n" +
                "\tt.nama_product as namaProduct,\n" +
                "\tt.harga_jual as hargaJual,\n" +
                "\tcoalesce((select disc_product from trdiskonproduk where id_product = t.i_id and current_timestamp::date between tanggal_awal_disc::date and tanggal_akhir_disc::date),0) as disc,\n" +
                "\tt.berat,\n" +
                "\tt.barcode\n" +
                "from\n" +
                "\ttrproduct t\n" +
                "where t.barcode = :barcode";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("barcode", barcode);
        try {
            ProdukDto.ProdukKasir data = jdbcTemplate.queryForObject(query, map, new BeanPropertyRowMapper<>(ProdukDto.ProdukKasir.class));
            if(data != null){
                return Optional.of(data);
            }else{
                return Optional.empty();
            }
        } catch(DataAccessException e){
            return Optional.empty();
        }
    }

    public List<ProdukDto.ProdukKasir> findProdukBySearchName(String paramName){
        String baseQuery = "select\n" +
                "\tt.i_id as id,\n" +
                "\tt.kode_product as kodeProduct,\n" +
                "\tt.nama_product as namaProduct,\n" +
                "\tt.harga_jual as hargaJual,\n" +
                "\tcoalesce((select disc_product from trdiskonproduk where id_product = t.i_id and current_timestamp::date between tanggal_awal_disc::date and tanggal_akhir_disc::date),0) as disc,\n" +
                "\tt.berat,\n" +
                "\tt.barcode\n" +
                "from\n" +
                "\ttrproduct t\n" +
                "where 1 = 1 \t";
        StringBuilder query = new StringBuilder(baseQuery);

        MapSqlParameterSource map = new MapSqlParameterSource();
        if(paramName != null){
            query.append(" and lower(nama_product) like lower(:paramName)");
            map.addValue("paramName", new StringBuilder("%")
                    .append(paramName).append("%").toString());
        }
        return jdbcTemplate.query(query.toString(), map, new BeanPropertyRowMapper<>(ProdukDto.ProdukKasir.class));
    }

}
