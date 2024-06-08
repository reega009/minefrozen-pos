package com.minefrozen.pos.service;

import com.minefrozen.pos.dao.*;
import com.minefrozen.pos.dto.ReturnDto;
import com.minefrozen.pos.dto.ServerDto;
import com.minefrozen.pos.dto.TransaksiDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PosToServerService {

    @Autowired
    private PosToServerDao dao;

    @Autowired
    private TrnomaxDao noMaxDao;

    @Autowired
    private ServerDao serverDao;

    @Autowired
    private ReturnDao returnDao;

    @Transactional("serverTransaction")
    public void TambahTransaksiServer(TransaksiDto.TambahTransaksi request){
        try{
            dao.tambahTransaksiServer(request);

            // Save Piutang if jenisPembayaran 4
            if(request.getJenisPembayaran() == 4){
                Integer newId = noMaxDao.findNoMaxServer("tmpiutang");

                ServerDto.TambahPiutang dataPiutang = new ServerDto.TambahPiutang();
                dataPiutang.setId(newId);
                dataPiutang.setIdTransaksi(request.getId());
                dataPiutang.setIdStore(request.getIdStore());
                dataPiutang.setTotalPelunasan(request.getTotalHargaPerTransaksi());
                dao.tambahPiutang(dataPiutang);

                noMaxDao.updateTrNomaxServer("tmpiutang");
            }

            // Save Rinci
            for (TransaksiDto.TambahTransaksiRinci rinci : request.getTransaksiRinci()){
                dao.tambahTransaksiRinciServer(rinci);

                // Update Invent Server Or Delete Invent Server
                BigDecimal remainingQuantity = rinci.getQty(); // 1

                List<TransaksiDto.ListSubstractInventory> listSubstractInventories = dao.listSubstractInventory(rinci.getIdProduk(), rinci.getIdStore()); // 20
                for (TransaksiDto.ListSubstractInventory listSubstract : listSubstractInventories){
                    log.info("Remaining Quantity: %d", remainingQuantity);
                    if(remainingQuantity.compareTo(BigDecimal.ZERO) <= 0){
                        break;
                    }

                    BigDecimal subtractedQuantity = remainingQuantity.subtract(listSubstract.getQty()) ; // 1 - 20 = -19
                    if(subtractedQuantity.compareTo(BigDecimal.ZERO) >= 0){
                        dao.deleteInvenServer(listSubstract.getIdInventory(), rinci.getIdStore(), listSubstract.getExpiredDate());
                        remainingQuantity = subtractedQuantity;
                        log.info("Delete ITEM {}", listSubstract);
                    }else{
                        dao.updateQtyInvenServer(rinci.getIdStore(), rinci.getIdProduk(), listSubstract.getExpiredDate(), subtractedQuantity);
                        remainingQuantity = subtractedQuantity;
                        log.info("UPDATE ITEM {}", listSubstract);
                    }
                }
            }
        }catch (Exception e){
            try{
                dao.tambahTransaksiBackup(request);

                // Save Rinci
                for (TransaksiDto.TambahTransaksiRinci rinci : request.getTransaksiRinci()){
                    dao.tambahTransaksiRinciBackup(rinci);
                }
            }catch (Exception exc){
                log.info("Error Saving Backup: " + e.getMessage());
                throw e;
            }
        }
    }

    @Transactional("serverTransaction")
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

                // Save Piutang if jenisPembayaran 4
                if(data.getJenisPembayaran() == 4){
                    Integer newId = noMaxDao.findNoMaxServer("tmpiutang");

                    ServerDto.TambahPiutang dataPiutang = new ServerDto.TambahPiutang();
                    dataPiutang.setId(newId);
                    dataPiutang.setIdTransaksi(data.getId());
                    dataPiutang.setIdStore(data.getIdStore());
                    dataPiutang.setTotalPelunasan(data.getTotalHargaPerTransaksi());
                    dao.tambahPiutang(dataPiutang);

                    noMaxDao.updateTrNomaxServer("tmpiutang");
                }

                // Save Rinci
                for (TransaksiDto.TambahTransaksiRinci rinci : data.getTransaksiRinci()){
                    dao.tambahTransaksiRinciServer(rinci);

                    // Update Invent Server Or Delete Invent Server
                    BigDecimal remainingQuantity = rinci.getQty(); // 20

                    List<TransaksiDto.ListSubstractInventory> listSubstractInventories = dao.listSubstractInventory(rinci.getIdProduk(), rinci.getIdStore()); // 30
                    for (TransaksiDto.ListSubstractInventory listSubstract : listSubstractInventories){
                        log.info("Remaining Quantity: %d", remainingQuantity);
                        if(remainingQuantity.compareTo(BigDecimal.ZERO) <= 0){
                            break;
                        }

                        BigDecimal subtractedQuantity = remainingQuantity.subtract(listSubstract.getQty()) ; // 20 - 30 = -10
                        if(subtractedQuantity.compareTo(BigDecimal.ZERO) >= 0){
                            dao.deleteInvenServer(listSubstract.getIdInventory(), rinci.getIdStore(), listSubstract.getExpiredDate());
                            remainingQuantity = subtractedQuantity;
                            log.info("Delete ITEM {}", listSubstract);
                        }else{
                            dao.updateQtyInvenServer(rinci.getIdStore(), rinci.getIdProduk(), listSubstract.getExpiredDate(), subtractedQuantity);
                            remainingQuantity = subtractedQuantity;
                            log.info("UPDATE ITEM {}", listSubstract);
                        }
                    }
                }
            }

            // Kosongkan Backup
            dao.deleteDataBackup();
            dao.deleteDataBackupRinci();
        }catch (Exception e){
            log.info("Error Send Data Backup To Server", e.getMessage());
            throw e;

        }
    }


    // RETURN
    @Transactional("serverTransaction")
    public void returnToServer(ReturnDto.Return data)throws Exception{
        // Check QTY Rinci POS
        Optional<BigDecimal> qtyRinci = returnDao.findTransaksiRinciForCheckQty(data.getIdProduk(), data.getIdTransaksi());

        // Update Total Harga Transaksi Server / Backup
        Optional<Integer> checkExists = returnDao.findExistsTransaksiServer(data.getIdTransaksi(), data.getIdStore());
        if(checkExists != null && !checkExists.isEmpty()){
            returnDao.saveReturnServer(data);

            if(qtyRinci.get() == data.getQty()){
                returnDao.deleteTransaksiRinciServer(data.getIdTransaksi(),data.getIdProduk(),data.getIdStore());
            }else{
                // Update QTY Rinci
                returnDao.updateQtyTransaksiRinciServer(data.getQty(), data.getTotalReturn(), data.getIdTransaksi(), data.getIdProduk());
            }

            returnDao.updateTotalHargaTransaksiServer(data.getIdTransaksi(), data.getIdStore(), data.getTotalReturn());

            // Update or save Inventory Server
            Optional<Integer> findExist = returnDao.findProdukByIdAndExpired(data.getIdProduk(), data.getExpiredDate(), data.getIdStore());
            if(findExist != null && !findExist.isEmpty()){
                returnDao.updateQtyInven(findExist.get(), data.getQty());
            }else{
                // Get Nomax
                Integer newIdInven = noMaxDao.findNoMaxServer("tminventory");

                ReturnDto.Inventory request = new ReturnDto.Inventory();
                request.setIdProduk(data.getIdProduk());
                request.setIdStore(data.getIdStore());
                request.setQty(data.getQty());
                request.setExpiredDate(data.getExpiredDate());
                request.setId(newIdInven);
                returnDao.saveInventory(request);
                // Update Nomax
                noMaxDao.updateTrNomaxServer("tminventory");
            }
        }else{
            throw new Exception("Data not found. Please send backup first.");
        }
    }

    // Change Tipe Pembayaran
    @Transactional("serverTransaction")
    public void changeTipePembayaran(TransaksiDto.ChangeTipePembayaran data)throws  Exception{
        Optional<Integer> checkExists = returnDao.findExistsTransaksiServer(data.getIdTransaksi(), data.getIdStore());
        if(checkExists != null && !checkExists.isEmpty()){
            dao.changeTipePembayaran(data);
        }else{
            throw new Exception("Data not found. Please send backup first.");
        }
    }

    public Integer testServer(){
        return dao.testServer();
    }

}
