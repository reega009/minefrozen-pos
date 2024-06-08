package com.minefrozen.pos.service;

import com.minefrozen.pos.dao.ReturnDao;
import com.minefrozen.pos.dao.TrnomaxDao;
import com.minefrozen.pos.dto.ReturnDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReturnService {

    @Autowired
    private ReturnDao dao;

    @Autowired
    private TrnomaxDao noMaxDao;

    @Autowired
    private PosToServerService serverService;

    public List<ReturnDto.FindAllReturn> findAllReturn(Integer idStore){
        return dao.findAllReturn(idStore);
    }


    public List<ReturnDto.FindTransaksiReturn> findTransaksiReturn(String kodeTransaksi, Integer idStore){
        return dao.findTransaksiReturn(kodeTransaksi, idStore);
    }

    @Transactional("posTransaction")
    public void saveReturn(ReturnDto.Return data) throws Exception{
        try {
            // Check Exists in server
            Optional<Integer> checkExists = dao.findExistsTransaksiServer(data.getIdTransaksi(), data.getIdStore());
            if(checkExists != null && !checkExists.isEmpty()){
                // Save Return
                Integer newId = noMaxDao.findNoMax("tmreturn");
                data.setId(newId);
                dao.saveReturn(data);
                noMaxDao.updateTrNomax("tmreturn");

                // Check QTY Trx RINCI
                Optional<BigDecimal> qtyRinci = dao.findTransaksiRinciForCheckQty(data.getIdProduk(), data.getIdTransaksi());
                if(qtyRinci.get() == data.getQty()){
                    // Delete Transaksi rinci
                    dao.deleteTransaksiRinci(data.getIdTransaksi(),data.getIdProduk(),data.getIdStore());
                }else{
                    // Update QTY Rinci
                    dao.updateQtyTransaksiRinci(data.getQty(), data.getTotalReturn(), data.getIdTransaksi(), data.getIdProduk());
                }

                // Update Total Harga Transaksi
                dao.updateTotalHargaTransaksi(data.getIdTransaksi(), data.getIdStore(), data.getTotalReturn());

                // Send Data to server
                serverService.returnToServer(data);
            }else{
                throw new Exception("Data not found. Please send backup first.");
            }
        }catch (Exception e){
            throw e;
        }
    }

}
