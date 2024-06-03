package com.minefrozen.pos.service;

import com.minefrozen.pos.dao.SaldoAwalDao;
import com.minefrozen.pos.dto.SaldoAwalDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@Service
public class SaldoAwalService {

    @Autowired
    private SaldoAwalDao dao;

    public Optional<SaldoAwalDto.SaldoAwal> findByTanggalSekarang(Integer nomorKasir, Integer idStore, Date tanggalSekarang){
        return dao.findByTanggalSekarang(nomorKasir, idStore, tanggalSekarang);
    }

    @Transactional
    public void save(SaldoAwalDto.SaldoAwal request){
        // Check Exists
        Optional<SaldoAwalDto.SaldoAwal> data = dao.findByTanggalSekarang(request.getNomorKasir(), request.getIdStore(), new Timestamp(System.currentTimeMillis()));
        if(data.isPresent()){
            dao.update(request);
            dao.updateServer(request);
        }else{
            dao.save(request);
            dao.saveServer(request);
        }
    }

}
