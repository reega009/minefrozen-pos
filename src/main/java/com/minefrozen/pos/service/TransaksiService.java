package com.minefrozen.pos.service;

import com.minefrozen.pos.dao.TransaksiDao;
import com.minefrozen.pos.dao.TrnomaxDao;
import com.minefrozen.pos.dto.CostumMessage;
import com.minefrozen.pos.dto.TransaksiDto;
import com.minefrozen.pos.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TransaksiService {

    @Autowired
    private TransaksiDao dao;

    @Autowired
    private TrnomaxDao noMaxDao;

    @Autowired
    private PosToServerService serverService;

    @Transactional("posTransaction")
    public String TambahTransaksi(TransaksiDto.TambahTransaksi request){

        // GET DATE NOW
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String formattedDate = currentDate.format(formatter);

        // Set DPgunRekam
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());


        try{
            // Get Nomor Transaksi Untuk Kode
            Integer newNomor = dao.countTransaksiToday(request.getIdStore());

            // Get Nomax
            Integer newId = noMaxDao.findNoMax("tmtransaksi");

            // Create Kode
            String newKode = String.format("TRX-%s-%04d", formattedDate, newNomor);
            log.info("New Kode : {}, New Id : {}", newKode, newId);

            // Check Trx Handle For Duplicate
            Optional<Integer> checkDupl = dao.checkIfTrxExist(request.getIdStore(), request.getNomorKasir(), currentTimestamp, request.getJenisPembayaran(), request.getNamaKasir(), request.getTotalHargaPerTransaksi());
            if (checkDupl.isPresent()) {
                log.info("TRANSAKSI DUPLICATE!!! {}", newKode);
                return  "Transaksi Duplicate Terdeteksi!";
            }

            // Save Proses
            request.setId(newId);
            request.setKodeTransaksi(newKode);
            request.setShift(1); // SEMENTARA HARDCODE
            request.setDPgunRekam(currentTimestamp);
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

            return newKode;
        }catch (Exception e){
            log.info("An error occurred: " + e.getMessage());
            throw e;
        }
    }

    public List<TransaksiDto.Transaksi> findAll(Integer idStore, Integer nomorKasir, Integer shift, Date tanggalAwal, Date tanggalAkhir){
        return dao.findAll(idStore, nomorKasir, shift, tanggalAwal, tanggalAkhir);
    }

    public List<TransaksiDto.TransaksiRinci> findAllRinci(Integer idStore, Integer idTransaksi){
        return dao.findAllRinci(idStore, idTransaksi);
    }

    @Transactional("posTransaction")
    public void changeTipePembayaran(TransaksiDto.ChangeTipePembayaran data) throws Exception{
        try{
            dao.changeTipePembayaran(data);
            serverService.changeTipePembayaran(data);
        }catch (Exception e){
            throw e;
        }
    }

}
