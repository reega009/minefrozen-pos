package com.minefrozen.pos.service;

import com.minefrozen.pos.dao.ReturnDao;
import com.minefrozen.pos.dao.TrnomaxDao;
import com.minefrozen.pos.dto.ReturnDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReturnService {

    @Autowired
    private ReturnDao dao;

    @Autowired
    private TrnomaxDao noMaxDao;

    public List<ReturnDto.Return> findAllReturn(Integer idStore){
        return dao.findAllReturn(idStore);
    }


    public List<ReturnDto.FindTransaksiReturn> findTransaksiReturn(String kodeTransaksi, Integer idStore){
        return dao.findTransaksiReturn(kodeTransaksi, idStore);
    }

    @Transactional
    public void saveReturn(ReturnDto.Return data) throws Exception{
        try {
            // Save Return
            Integer newId = noMaxDao.findNoMax("tmreturn");
            data.setId(newId);
            dao.saveReturn(data);
            noMaxDao.updateTrNomax("tmreturn");

            // Delete Transaksi rinci
            dao.deleteTransaksiRinci(data.getIdTransaksi(),data.getIdProduk(),data.getExpiredDate(),data.getIdStore());

            // Update Total Harga Transaksi
            dao.updateTotalHargaTransaksi(data.getIdTransaksi(), data.getIdStore(), data.getTotalReturn());

            // Update Total Harga Transaksi Server / Backup
            Optional<Integer> checkExists = dao.findExistsTransaksiServer(data.getIdTransaksi(), data.getIdStore());
            if(checkExists != null && !checkExists.isEmpty()){
                dao.saveReturnServer(data);
                dao.deleteTransaksiRinciServer(data.getIdTransaksi(),data.getIdProduk(),data.getExpiredDate(),data.getIdStore());
                dao.updateTotalHargaTransaksiServer(data.getIdTransaksi(), data.getIdStore(), data.getTotalReturn());

                // Update or save Inventory Server
                Optional<Integer> findExist = dao.findProdukByIdAndExpired(data.getIdProduk(), data.getExpiredDate(), data.getIdStore());
                if(findExist != null && !findExist.isEmpty()){
                    dao.updateQtyInven(findExist.get(), data.getQty());
                }else{
                    // Get Nomax
                    Integer newIdInven = noMaxDao.findNoMaxServer("tminventory");

                    ReturnDto.Inventory request = new ReturnDto.Inventory();
                    request.setIdProduk(data.getIdProduk());
                    request.setIdStore(data.getIdStore());
                    request.setQty(data.getQty());
                    request.setExpiredDate(data.getExpiredDate());
                    request.setId(newIdInven);
                    dao.saveInventory(request);
                    // Update Nomax
                    noMaxDao.updateTrNomaxServer("tminventory");
                }
            }else{
                throw new Exception("Data not found. Please send backup first.");
            }
        }catch (Exception e){
            throw e;
        }
    }

}
