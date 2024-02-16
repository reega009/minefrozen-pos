package com.minefrozen.pos.service;

import com.minefrozen.pos.dao.TransaksiDao;
import com.minefrozen.pos.dao.TrnomaxDao;
import com.minefrozen.pos.dto.TransaksiDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class TransaksiService {

    @Autowired
    private TransaksiDao dao;

    @Autowired
    private TrnomaxDao noMaxDao;

    @Autowired
    private PosToServerService serverService;

    @Transactional
    public void TambahTransaksi(TransaksiDto.TambahTransaksi request){

        // GET DATE NOW
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String formattedDate = currentDate.format(formatter);

        Instant instantNow = Instant.now();
        LocalDateTime timeStampNow = LocalDateTime.ofInstant(instantNow, ZoneId.systemDefault());


        try{
            // Get Nomor Transaksi Untuk Kode
            Integer newNomor = dao.countTransaksiToday(request.getIdStore());

            // Get Nomax
            Integer newId = noMaxDao.findNoMax("tmtransaksi");

            // Create Kode
            String newKode = String.format("TRX-%s-%04d", formattedDate, newNomor);
            log.info("New Kode : {}, New Id : {}", newKode, newId);

            // Save Proses
            request.setId(newId);
            request.setKodeTransaksi(newKode);
            request.setDPgunRekam(Timestamp.valueOf(timeStampNow));
            dao.tambahTransaksi(request);

            // Save Rinci
            for (TransaksiDto.TambahTransaksiRinci rinci : request.getTransaksiRinci()){
                    rinci.setIdTransaksi(newId);
                    dao.tambahTransaksiRinci(rinci);
            }

            // Save Server
            serverService.TambahTransaksiServer(request);

            // Update TRNOMAX
            noMaxDao.updateTrNomax("tmtransaksi");

        }catch (Exception e){
            log.info("An error occurred: " + e.getMessage());
            throw e;
        }
    }

    public List<TransaksiDto.Transaksi> findAll(Integer idStore, Integer nomorKasir, Integer shift){
        return dao.findAll(idStore, nomorKasir, shift);
    }

    public List<TransaksiDto.TransaksiRinci> findAllRinci(Integer idStore, Integer idTransaksi){
        return dao.findAllRinci(idStore, idTransaksi);
    }

}
