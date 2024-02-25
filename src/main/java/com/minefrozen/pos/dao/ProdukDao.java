package com.minefrozen.pos.dao;

import com.minefrozen.pos.dto.DiskonProdukDto;
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

    public Optional<ProdukDto.ProdukKasir> findProdukByBarcode(BigInteger barcode){
        String query = "select\n" +
                "\tt.i_id as id,\n" +
                "\tt.kode_product as kodeProduct,\n" +
                "\tt.nama_product as namaProduct,\n" +
                "\tt.harga_jual as hargaJual,\n" +
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


    public List<ProdukDto.Produk> findAll(){
        String query = "select\n" +
                "\tt.i_id as id,\n" +
                "\tt.kode_product as kodeProduct,\n" +
                "\tt.nama_product as namaProduct,\n" +
                "\tt.barcode,\n" +
                "\tt.berat,\n" +
                "\tt.harga_beli as hargaBeli,\n" +
                "\tt.harga_jual as hargaJual,\n" +
                "\tt.i_pgun_rekam as iPgunRekam,\n" +
                "\tt.d_pgun_rekam as dPgunRekam,\n" +
                "\tt.i_pgun_ubah as iPgunUbah,\n" +
                "\tt.d_pgun_ubah as dPgunUbah\n" +
                "from\n" +
                "\ttrproduct t";
        MapSqlParameterSource map = new MapSqlParameterSource();
        return jdbcTemplate.query(query, map, new BeanPropertyRowMapper<>(ProdukDto.Produk.class));
    }


    public Optional<ProdukDto.Produk> findById(Integer idProduk){
        String query = "select\n" +
                "\tt.i_id as id,\n" +
                "\tt.kode_product as kodeProduct,\n" +
                "\tt.nama_product as namaProduct,\n" +
                "\tt.barcode,\n" +
                "\tt.berat,\n" +
                "\tt.harga_beli as hargaBeli,\n" +
                "\tt.harga_jual as hargaJual,\n" +
                "\tt.i_pgun_rekam as iPgunRekam,\n" +
                "\tt.d_pgun_rekam as dPgunRekam,\n" +
                "\tt.i_pgun_ubah as iPgunUbah,\n" +
                "\tt.d_pgun_ubah as dPgunUbah\n" +
                "from\n" +
                "\ttrproduct t\n" +
                "where i_id = :idProduk";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idProduk",idProduk);
        try {
            ProdukDto.Produk data = jdbcTemplate.queryForObject(query, map, new BeanPropertyRowMapper<>(ProdukDto.Produk.class));
            if(data != null){
                return Optional.of(data);
            }else{
                return Optional.empty();
            }
        } catch(DataAccessException e){
            return Optional.empty();
        }
    }

    public void save(ProdukDto.Produk data){
        String query = "insert\n" +
                "\tinto\n" +
                "\ttrproduct\n" +
                "(kode_product,\n" +
                "\tnama_product,\n" +
                "\tbarcode,\n" +
                "\tberat,\n" +
                "\tharga_beli,\n" +
                "\tharga_jual,\n" +
                "\ti_pgun_rekam,\n" +
                "\td_pgun_rekam,\n" +
                "\tmin_jumlah_barang)\n" +
                "values(:kodeProduct,\n" +
                ":namaProduct,\n" +
                ":barcode,\n" +
                ":berat,\n" +
                ":hargaBeli,\n" +
                ":hargaJual,\n" +
                ":iPgunRekam,\n" +
                "CURRENT_TIMESTAMP,\n" +
                ":minJumlahBarang)\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("kodeProduct", data.getKodeProduct());
        map.addValue("namaProduct", data.getNamaProduct());
        map.addValue("barcode", data.getBarcode());
        map.addValue("berat", data.getBerat());
        map.addValue("hargaBeli", data.getHargaBeli());
        map.addValue("hargaJual", data.getHargaJual());
        map.addValue("iPgunRekam", 1);
        map.addValue("minJumlahBarang", data.getMinJumlahBarang());
        jdbcTemplate.update(query,map);
    }

    public void update(ProdukDto.Produk data){
        String query = "update\n" +
                "\ttrproduct\n" +
                "set\n" +
                "\tkode_product = :kodeProduct,\n" +
                "\tnama_product = :namaProduct,\n" +
                "\tbarcode = :barcode,\n" +
                "\tberat = :berat,\n" +
                "\tharga_beli = :hargaBeli,\n" +
                "\tharga_jual = :hargaJual,\n" +
                "\ti_pgun_ubah = :iPgunUbah,\n" +
                "\td_pgun_ubah = current_timestamp,\n" +
                "\tmin_jumlah_barang = :minJumlahBarang\n" +
                "where i_id = :idProduk";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idProduk", data.getId());
        map.addValue("kodeProduct", data.getKodeProduct());
        map.addValue("namaProduct", data.getNamaProduct());
        map.addValue("barcode", data.getBarcode());
        map.addValue("berat", data.getBerat());
        map.addValue("hargaBeli", data.getHargaBeli());
        map.addValue("hargaJual", data.getHargaJual());
        map.addValue("iPgunUbah", data.getIPgunUbah());
        map.addValue("minJumlahBarang", data.getMinJumlahBarang());
        jdbcTemplate.update(query,map);
    }

    public void delete(Integer id){
        String query = "delete from trproduct where i_id = :id";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id",id);
        jdbcTemplate.update(query,map);
    }


}
