package com.minefrozen.pos.controller;

import com.minefrozen.pos.dto.CostumMessage;
import com.minefrozen.pos.dto.MemberDto;
import com.minefrozen.pos.dto.SaldoAwalDto;
import com.minefrozen.pos.dto.UserDto;
import com.minefrozen.pos.service.SaldoAwalService;
import com.minefrozen.pos.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api-pos/modal-awal")
@Slf4j
public class SaldoAwalController {

    @Autowired
    private SaldoAwalService service;

    @Autowired
    private UserService userService;

    @GetMapping("/findByTanggalSekarang")
    public ResponseEntity<?> findById(@RequestParam Integer nomorKasir, @RequestParam Integer idStore, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date tanggalSekarang){
        Optional<SaldoAwalDto.SaldoAwal> data = service.findByTanggalSekarang(nomorKasir, idStore, tanggalSekarang);
        if (!data.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(data.get());
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody SaldoAwalDto.SaldoAwal data){
        // Get User Input
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<UserDto.UserLogin> pengguna = userService.findByUsername(username);
        if (!pengguna.isPresent()) {
            return ResponseEntity.badRequest().body(new CostumMessage(HttpStatus.BAD_REQUEST, "Pengguna Login Tidak Ditemukan"));
        }
        data.setIPgunRekam(pengguna.get().getId());

        try {
            service.save(data);
            return ResponseEntity.ok(new CostumMessage(HttpStatus.OK, "Data Tersimpan"));
        } catch (Exception throwables) {
            throwables.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CostumMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Terjadi Kesalahan Server"));
        }
    }

}
