package com.minefrozen.pos.controller;


import com.minefrozen.pos.dto.LaporanCashierDto;
import com.minefrozen.pos.dto.RekapTransaksiDto;
import com.minefrozen.pos.service.LaporanCashierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api-pos/laporan-cashier")
public class LaporanCashierController {

    @Autowired
    private LaporanCashierService service;

    @GetMapping("/findLaporanCashier")
    public ResponseEntity<?> findAll(@RequestParam Integer idStore, @RequestParam  Date tanggalAwal, @RequestParam Date tanggalAkhir){
        List<LaporanCashierDto.LaporanCashier> data = service.findAll(idStore, tanggalAwal, tanggalAkhir);
        return ResponseEntity.ok(data);
    }

}
