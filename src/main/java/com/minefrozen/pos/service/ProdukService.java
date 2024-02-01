package com.minefrozen.pos.service;

import com.minefrozen.pos.dao.ProdukDao;
import com.minefrozen.pos.dao.ServerDao;
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
    private ServerDao serverDao;

    @Transactional
    public List<ProdukDto.ProdukKasir> findProdukByBarcode(BigInteger barcode, Integer idStore){
        try{
            return serverDao.findProdukByBarcodeServer(barcode, idStore);
        }catch (Exception e){
            return dao.findProdukByBarcode(barcode);
        }
    }

    @Transactional
    public List<ProdukDto.ProdukKasir> findProdukBySearchName(String paramName, Integer idStore){
        try{
            return serverDao.findProdukBySearchNameServer(paramName,idStore);
        }catch (Exception e){
            return dao.findProdukBySearchName(paramName);
        }
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



}
