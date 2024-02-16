package com.minefrozen.pos.service;


import com.minefrozen.pos.dao.LaporanCashierDao;
import com.minefrozen.pos.dto.LaporanCashierDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class LaporanCashierService {

    @Autowired
    private LaporanCashierDao dao;

    public List<LaporanCashierDto.LaporanCashier> findAll(Integer idStore, Date tanggalAwal, Date tanggalAkhir){
        return dao.findAll(idStore, tanggalAwal, tanggalAkhir);
    }

}
