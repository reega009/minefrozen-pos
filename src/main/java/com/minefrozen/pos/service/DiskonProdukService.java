package com.minefrozen.pos.service;

import com.minefrozen.pos.dao.DiskonProdukDao;
import com.minefrozen.pos.dto.DiskonProdukDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DiskonProdukService {

    @Autowired
    private DiskonProdukDao dao;

    public List<DiskonProdukDto.DiskonProduk> findAll(Integer idStore){
        return dao.findAll(idStore);
    }


    public Optional<DiskonProdukDto.DiskonProduk> findById(Integer id, Integer idStore){
        return dao.findById(id, idStore);
    }

    public Optional<DiskonProdukDto.DiskonProduk> findCheckDisc(Integer idStore,
                                                                Integer idProduk,
                                                                Integer qtyBeli){
        return dao.findCheckDisc(idStore, idProduk, qtyBeli);
    }

    public void save(DiskonProdukDto.DiskonProduk data){
        dao.save(data);
    }

    public void update(DiskonProdukDto.DiskonProduk data){
        dao.update(data);
    }

    public void delete(Integer id){
        dao.delete(id);
    }

}
