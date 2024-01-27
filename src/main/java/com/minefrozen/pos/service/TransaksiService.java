package com.minefrozen.pos.service;

import com.minefrozen.pos.dao.TransaksiDao;
import com.minefrozen.pos.dao.TrnomaxDao;
import com.minefrozen.pos.dto.TransaksiDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
        }


    }

}
