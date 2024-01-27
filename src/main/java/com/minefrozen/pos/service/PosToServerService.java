package com.minefrozen.pos.service;

import com.minefrozen.pos.dao.PosToServerDao;
import com.minefrozen.pos.dao.ServerDao;
import com.minefrozen.pos.dao.TransaksiDao;
import com.minefrozen.pos.dao.TrnomaxDao;
import com.minefrozen.pos.dto.ServerDto;
import com.minefrozen.pos.dto.TransaksiDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class PosToServerService {

    @Autowired
    private PosToServerDao dao;

    @Autowired
    private TrnomaxDao noMaxDao;

    @Autowired
    private ServerDao serverDao;

    @Transactional
    public void TambahTransaksiServer(TransaksiDto.TambahTransaksi request){
        try{
            dao.tambahTransaksiServer(request);

            // Save Rinci
            for (TransaksiDto.TambahTransaksiRinci rinci : request.getTransaksiRinci()){
                dao.tambahTransaksiRinciServer(rinci);

                // Update Invent Server
                dao.updateQtyInvenServer(rinci.getIdStore(), rinci.getIdProduk(), rinci.getExpiredDate(), rinci.getQty());
            }


        }catch (Exception e){
            log.info("Error Saving Server, try Saving Backup : " + e.getMessage());
            try{
                dao.tambahTransaksiBackup(request);

                // Save Rinci
                for (TransaksiDto.TambahTransaksiRinci rinci : request.getTransaksiRinci()){
                    dao.tambahTransaksiRinciBackup(rinci);
                }
            }catch (Exception exc){
                log.info("Error Saving Backup: " + e.getMessage());
            }
        }
    }

    // Send Data Backup To Server
    public void sendDataBackupToServer(){
        try{

            // Get transaksi
            List<TransaksiDto.TambahTransaksi> dataTransaksi = dao.findAllTransaksiBackup();

            // Get Transaksi Rinci
            for (TransaksiDto.TambahTransaksi data : dataTransaksi){
                List<TransaksiDto.TambahTransaksiRinci> dataTransaksiRinci = dao.findAllTransaksiRinciBackup(data.getId());
                data.setTransaksiRinci(dataTransaksiRinci);


                // Send to Server
                dao.tambahTransaksiServer(data);
                for (TransaksiDto.TambahTransaksiRinci dataRinci : data.getTransaksiRinci()){
                    dao.tambahTransaksiRinciServer(dataRinci);
                }
            }

            // Kosongkan Backup
            dao.deleteDataBackup();
            dao.deleteDataBackupRinci();
        }catch (Exception e){
            log.info("Error Send Data Backup To Server", e.getMessage());
        }
    }

}
