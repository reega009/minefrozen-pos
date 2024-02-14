package com.minefrozen.pos.controller;


import com.minefrozen.pos.dto.RekapTransaksiDto;
import com.minefrozen.pos.service.RekapTransaksiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/rekap-transaksi")
public class RekapTransaksiController {


    @Autowired
    private RekapTransaksiService service;

    @GetMapping("/findRekapTransaksi")
    public ResponseEntity<?> findAll(@RequestParam Integer idStore, @RequestParam Integer nomorKasir, @RequestParam Integer shift, @RequestParam Date tanggal){
        List<RekapTransaksiDto.Transaksi> data = service.findAll(idStore, nomorKasir, shift , tanggal);
        return ResponseEntity.ok(data);
    }


}
