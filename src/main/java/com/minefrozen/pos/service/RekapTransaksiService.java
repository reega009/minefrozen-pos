package com.minefrozen.pos.service;

import com.minefrozen.pos.dao.RekapTransaksiDao;
import com.minefrozen.pos.dto.RekapTransaksiDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
@Slf4j
public class RekapTransaksiService {


    @Autowired
    private RekapTransaksiDao dao;

    public List<RekapTransaksiDto.Transaksi> findAll(Integer idStore, Integer nomorKasir, Integer shift, Date tanggal){
        return dao.findAll(idStore, nomorKasir, shift, tanggal );
    }



}
