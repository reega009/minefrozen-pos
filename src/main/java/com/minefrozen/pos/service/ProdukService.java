package com.minefrozen.pos.service;

import com.minefrozen.pos.dao.DiskonProdukDao;
import com.minefrozen.pos.dao.ProdukDao;
import com.minefrozen.pos.dao.ServerDao;
import com.minefrozen.pos.dto.DiskonProdukDto;
import com.minefrozen.pos.dto.ProdukDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProdukService {

    @Autowired
    private ProdukDao dao;

    @Autowired
    private DiskonProdukDao diskonDao;

    @Autowired
    private ServerDao serverDao;

    public Optional<ProdukDto.ProdukKasir> findProdukByBarcode(String barcode){
        Optional<ProdukDto.ProdukKasir> dataProduk = dao.findProdukByBarcode(barcode);

        Optional<DiskonProdukDto.DiskonProduk> disc = diskonDao.findCheckDisc(dataProduk.get().getId());
        if(disc.isPresent()){
            dataProduk.get().setIsDiskon(true);
            dataProduk.get().setJenisDiskon(disc.get().getJenisDiskon());
            dataProduk.get().setDiscProduk(disc.get().getDiscProduk());
            dataProduk.get().setIdProdukBonus(disc.get().getIdProdukBonus());
            dataProduk.get().setMinQtyToGetBonus(disc.get().getMinQtyToGetBonus());
        }else{
            dataProduk.get().setIsDiskon(false);
        }
        return dataProduk;
    }

    public Optional<ProdukDto.ProdukKasir> findProdukBySearchName(String paramName){
        Optional<ProdukDto.ProdukKasir> dataProduk = dao.findProdukBySearchName(paramName);

        Optional<DiskonProdukDto.DiskonProduk> disc = diskonDao.findCheckDisc(dataProduk.get().getId());
        if(disc.isPresent()){
            dataProduk.get().setIsDiskon(true);
            dataProduk.get().setJenisDiskon(disc.get().getJenisDiskon());
            dataProduk.get().setDiscProduk(disc.get().getDiscProduk());
            dataProduk.get().setIdProdukBonus(disc.get().getIdProdukBonus());
            dataProduk.get().setMinQtyToGetBonus(disc.get().getMinQtyToGetBonus());
        }else{
            dataProduk.get().setIsDiskon(false);
        }
        return dataProduk;
    }

    public List<ProdukDto.Produk> findAll(){
        return dao.findAll();
    }
    public Optional<ProdukDto.Produk> findById(Integer id){
        return dao.findById(id);
    }

    public void save(ProdukDto.Produk data){
        dao.save(data);
    }

    public void update(ProdukDto.Produk data){
        dao.update(data);
    }

    public void delete(Integer id){
        dao.delete(id);
    }

    // Matching data with Server
    @Transactional("posTransaction")
    public void matchingDataWithServer(){
        try {
            List<ProdukDto.Produk> datas = dao.findAllServer();

            if(!datas.isEmpty()){
                dao.deleteAllProduct();
                for (ProdukDto.Produk data : datas){
                    dao.save(data);
                }
            }
        }catch (Exception e){
            log.error("An error occurred: " + e.getMessage());
            throw e;
        }
    }

}
