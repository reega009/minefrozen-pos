package com.minefrozen.pos.service;

import com.minefrozen.pos.dao.ProdukDao;
import com.minefrozen.pos.dto.ProdukDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public class ProdukService {

    @Autowired
    private ProdukDao dao;

    public Optional<ProdukDto.ProdukKasir> findProdukByBarcode(String barcode){
        return dao.findProdukByBarcode(barcode);
    }

    public List<ProdukDto.ProdukKasir> findProdukBySearchName(String paramName){
        return dao.findProdukBySearchName(paramName);
    }

}
