package com.minefrozen.pos.controller;

import com.minefrozen.pos.dto.CostumMessage;
import com.minefrozen.pos.dto.DiskonProdukDto;
import com.minefrozen.pos.dto.MemberDto;
import com.minefrozen.pos.service.DiskonProdukService;
import com.minefrozen.pos.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pos/diskon")
public class DiskonProdukController {

    @Autowired
    private DiskonProdukService service;


    @GetMapping("/findAll")
    public ResponseEntity<?> findAll(@RequestParam Integer idStore){
        List<DiskonProdukDto.DiskonProduk> data = service.findAll(idStore);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam Integer id, @RequestParam Integer idStore){
        Optional<DiskonProdukDto.DiskonProduk> data = service.findById(id, idStore);
        if (!data.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(data.get());
    }

    @GetMapping("/findCheckDisc")
    public ResponseEntity<?> findCheckDisc(@RequestParam Integer idStore,
                                           @RequestParam Integer idProduk,
                                           @RequestParam Integer qtyBeli,
                                           @RequestParam Date expiredDateProduk){
        Optional<DiskonProdukDto.DiskonProduk> data = service.findCheckDisc(idStore, idProduk, qtyBeli, expiredDateProduk);
        if (!data.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(data.get());
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody DiskonProdukDto.DiskonProduk data){
        try {
            service.save(data);
            return ResponseEntity.ok(new CostumMessage(HttpStatus.OK, "Data Tersimpan"));
        } catch (Exception throwables) {
            throwables.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CostumMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Terjadi Kesalahan Server"));
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody DiskonProdukDto.DiskonProduk data){
        try {
            service.update(data);
            return ResponseEntity.ok(new CostumMessage(HttpStatus.OK, "Data Terupdate"));
        } catch (Exception throwables) {
            throwables.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CostumMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Terjadi Kesalahan Server"));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        try {
            service.delete(id);
            return ResponseEntity.ok(new CostumMessage(HttpStatus.OK, "Data Terhapus"));
        } catch (Exception throwables) {
            throwables.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CostumMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Terjadi Kesalahan Server"));
        }
    }

}
