package com.minefrozen.pos.controller;

import com.minefrozen.pos.dto.CostumMessage;
import com.minefrozen.pos.dto.DiskonProdukDto;
import com.minefrozen.pos.dto.ReturnDto;
import com.minefrozen.pos.service.ReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pos/return")
public class ReturnController {

    @Autowired
    private ReturnService service;


    @GetMapping("/findAll")
    public ResponseEntity<?> findAll(@RequestParam Integer idStore){
        List<ReturnDto.Return> data = service.findAllReturn(idStore);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/findTransaksiReturn")
    public ResponseEntity<?> findTransaksiReturn(@RequestParam String kodeTransaksi, @RequestParam Integer idStore){
        List<ReturnDto.FindTransaksiReturn> data = service.findTransaksiReturn(kodeTransaksi, idStore);
        return ResponseEntity.ok(data);
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody ReturnDto.Return data){
        try {
            service.saveReturn(data);
            return ResponseEntity.ok(new CostumMessage(HttpStatus.OK, "Data Tersimpan"));
        } catch (Exception e) {
            if (e.getMessage().equals("Data not found. Please send backup first.")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CostumMessage(HttpStatus.NOT_FOUND, e.getMessage()));
            } else {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CostumMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Terjadi Kesalahan Server"));
            }
        }
    }


}
