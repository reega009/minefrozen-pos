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
public class ServerDao {

    @Autowired
    @Qualifier("serverJdbc")
    private NamedParameterJdbcTemplate jdbcTemplate;

    public List<ProdukDto.ProdukKasir> findProdukByBarcodeServer(BigInteger barcode, Integer idStore){
        String query = "select\n" +
                "\tt.i_id as id,\n" +
                "\tt.kode_product as kodeProduct,\n" +
                "\tt.nama_product as namaProduct,\n" +
                "\tt.harga_jual as hargaJual,\n" +
                "\tt.berat,\n" +
                "\tt.barcode,\n" +
                "\ttinven.qty as qtyInven,\n" +
                "\ttinven.expired_date::date as expiredDate\n" +
                "from\n" +
                "\ttrproduct t,\n" +
                "\ttminventory tinven\n" +
                "where \n" +
                "\tt.i_id = tinven.id_produk\n" +
                "\tAND t.barcode = :barcode\n" +
                "\tand tinven.id_store = :idStore";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("barcode", barcode);
        map.addValue("idStore", idStore);
        return jdbcTemplate.query(query, map, new BeanPropertyRowMapper<>(ProdukDto.ProdukKasir.class));
    }

    public List<ProdukDto.ProdukKasir> findProdukBySearchNameServer(String paramName, Integer idStore){
        String baseQuery = "select\n" +
                "\tt.i_id as id,\n" +
                "\tt.kode_product as kodeProduct,\n" +
                "\tt.nama_product as namaProduct,\n" +
                "\tt.harga_jual as hargaJual,\n" +
                "\tt.berat,\n" +
                "\tt.barcode,\n" +
                "\ttinven.qty as qtyInven,\n" +
                "\ttinven.expired_date::date as expiredDate\n" +
                "from\n" +
                "\ttrproduct t,\n" +
                "\ttminventory tinven\n" +
                "where \n" +
                "\tt.i_id = tinven.id_produk\n" +
                "\tand tinven.id_store = :idStore";
        StringBuilder query = new StringBuilder(baseQuery);

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idStore", idStore);
        if(paramName != null){
            query.append(" and lower(nama_product) like lower(:paramName)");
            map.addValue("paramName", new StringBuilder("%")
                    .append(paramName).append("%").toString());
        }
        return jdbcTemplate.query(query.toString(), map, new BeanPropertyRowMapper<>(ProdukDto.ProdukKasir.class));
    }


}
