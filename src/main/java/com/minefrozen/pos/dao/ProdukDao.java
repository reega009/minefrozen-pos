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

    @Autowired
    @Qualifier("serverJdbc")
    private NamedParameterJdbcTemplate jdbcTemplateServer;

    public Optional<ProdukDto.ProdukKasir> findProdukByBarcode(String barcode){
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

    public Optional<ProdukDto.ProdukKasir> findProdukBySearchName(String paramName){
        String query = "select\n" +
                "\tt.i_id as id,\n" +
                "\tt.kode_product as kodeProduct,\n" +
                "\tt.nama_product as namaProduct,\n" +
                "\tt.harga_jual as hargaJual,\n" +
                "\tt.berat,\n" +
                "\tt.barcode\n" +
                "from\n" +
                "\ttrproduct t\n" +
                "where lower(nama_product) = lower(:paramName)";

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("paramName", paramName);
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


    public List<ProdukDto.Produk> findAll(){
        String query = "select\n" +
                "\tt.i_id as id,\n" +
                "\tt.kode_product as kodeProduct,\n" +
                "\tt.nama_product as namaProduct,\n" +
                "\tt.barcode,\n" +
                "\tt.id_supplier as idSupplier,\n" +
                "\tt.id_category as idCategory,\n" +
                "\tt.berat,\n" +
                "\tt.min_jumlah_barang as minJumlahBarang,\n" +
                "\tt.desc_product as descProduk,\n" +
                "\tt.harga_beli as hargaBeli,\n" +
                "\tt.harga_jual as hargaJual,\n" +
                "\tt.i_pgun_rekam as iPgunRekam,\n" +
                "\tt.d_pgun_rekam as dPgunRekam,\n" +
                "\tt.i_pgun_ubah as iPgunUbah,\n" +
                "\tt.d_pgun_ubah as dPgunUbah,\n" +
                "\tcoalesce(\n" +
                "\t\t(select true from tmdiskonproduk td where td.id_produk = t.i_id AND current_timestamp::date BETWEEN tanggal_awal_periode::date AND tanggal_akhir_periode::date order by id desc limit 1)\n" +
                "\t, false) as isDiskon,\n" +
                "\t(select jenis_diskon from tmdiskonproduk td where td.id_produk = t.i_id AND current_timestamp::date BETWEEN tanggal_awal_periode::date AND tanggal_akhir_periode::date order by id desc limit 1) as jenisDiskon,\n" +
                "\t(select coalesce(disc, 100) from tmdiskonproduk td where td.id_produk = t.i_id AND current_timestamp::date BETWEEN tanggal_awal_periode::date AND tanggal_akhir_periode::date order by id desc limit 1) as discProduk,\n" +
                "\t(select id_produk_bonus from tmdiskonproduk td where td.id_produk = t.i_id AND current_timestamp::date BETWEEN tanggal_awal_periode::date AND tanggal_akhir_periode::date order by id desc limit 1) as idProdukBonus,\n" +
                "\t(select syarat_qty_bonus from tmdiskonproduk td where td.id_produk = t.i_id AND current_timestamp::date BETWEEN tanggal_awal_periode::date AND tanggal_akhir_periode::date order by id desc limit 1) as minQtyToGetBonus\n" +
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
                "\tt.id_supplier as idSupplier,\n" +
                "\tt.id_category as idCategory,\n" +
                "\tt.berat,\n" +
                "\tt.min_jumlah_barang as minJumlahBarang,\n" +
                "\tt.desc_product as descProduk,\n" +
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
                "(\n" +
                "\ti_id,\n" +
                "\tkode_product,\n" +
                "\tnama_product,\n" +
                "\tbarcode,\n" +
                "\tid_supplier,\n" +
                "\tid_category,\n" +
                "\tberat,\n" +
                "\tharga_beli,\n" +
                "\tharga_jual,\n" +
                "\tmin_jumlah_barang,\n" +
                "\tdesc_product,\n" +
                "\ti_pgun_rekam,\n" +
                "\td_pgun_rekam,\n" +
                "\ti_pgun_ubah,\n" +
                "\td_pgun_ubah\n" +
                ")\n" +
                "values(\n" +
                "\t:id,\n" +
                "\t:kodeProduct,\n" +
                "\t:namaProduct,\n" +
                "\t:barcode,\n" +
                "\t:idSupplier,\n" +
                "\t:idCategory,\n" +
                "\t:berat,\n" +
                "\t:hargaBeli,\n" +
                "\t:hargaJual,\n" +
                "\t:minJumlahBarang,\n" +
                "\t:descProduk,\n" +
                "\t:iPgunRekam,\n" +
                "\t:dPgunRekam,\n" +
                "\t:iPgunUbah,\n" +
                "\t:dPgunUbah\n" +
                ")\n";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", data.getId());
        map.addValue("kodeProduct", data.getKodeProduct());
        map.addValue("namaProduct", data.getNamaProduct());
        map.addValue("barcode", data.getBarcode());
        map.addValue("idSupplier", data.getIdSupplier());
        map.addValue("idCategory", data.getIdCategory());
        map.addValue("berat", data.getBerat());
        map.addValue("hargaBeli", data.getHargaBeli());
        map.addValue("hargaJual", data.getHargaJual());
        map.addValue("minJumlahBarang", data.getMinJumlahBarang());
        map.addValue("descProduk", data.getDescProduk());
        map.addValue("iPgunRekam", data.getIPgunRekam());
        map.addValue("dPgunRekam", data.getDPgunRekam());
        map.addValue("iPgunUbah", data.getIPgunUbah());
        map.addValue("dPgunUbah", data.getDPgunUbah());
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

    public void deleteAllProduct(){
        String query = "delete from trproduct";
        MapSqlParameterSource map = new MapSqlParameterSource();
        jdbcTemplate.update(query,map);
    }

    // Server
    public List<ProdukDto.Produk> findAllServer() {
        String query = "select\n" +
                "\tt.i_id as id,\n" +
                "\tt.kode_product as kodeProduct,\n" +
                "\tt.nama_product as namaProduct,\n" +
                "\tt.barcode,\n" +
                "\tt.id_supplier as idSupplier,\n" +
                "\tt.id_category as idCategory,\n" +
                "\tt.berat,\n" +
                "\tt.min_jumlah_barang as minJumlahBarang,\n" +
                "\tt.desc_product as descProduk,\n" +
                "\tt.harga_beli as hargaBeli,\n" +
                "\tt.harga_jual as hargaJual,\n" +
                "\tt.i_pgun_rekam as iPgunRekam,\n" +
                "\tt.d_pgun_rekam as dPgunRekam,\n" +
                "\tt.i_pgun_ubah as iPgunUbah,\n" +
                "\tt.d_pgun_ubah as dPgunUbah\n" +
                "from\n" +
                "\ttrproduct t";
        MapSqlParameterSource map = new MapSqlParameterSource();
        return jdbcTemplateServer.query(query, map, new BeanPropertyRowMapper<>(ProdukDto.Produk.class));

    }


}
