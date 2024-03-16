package com.minefrozen.pos.controller;

import com.minefrozen.pos.dto.CostumMessage;
import com.minefrozen.pos.dto.ProdukDto;
import com.minefrozen.pos.dto.TransaksiDto;
import com.minefrozen.pos.dto.UserDto;
import com.minefrozen.pos.service.TransaksiService;
import com.minefrozen.pos.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pos/transaksi")
@Slf4j
public class TransaksiController {

    @Autowired
    private TransaksiService service;

    @Autowired
    private UserService userService;


    @PostMapping("/saveTransaksi")
    public ResponseEntity<?> saveTransaksi(@RequestBody TransaksiDto.TambahTransaksi data){
        // Get User Input
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<UserDto.UserLogin> pengguna = userService.findByUsername(username);
        if (!pengguna.isPresent()) {
            return ResponseEntity.badRequest().body(new CostumMessage(HttpStatus.BAD_REQUEST, "Pengguna Login Tidak Ditemukan"));
        }
        data.setIPgunRekam(pengguna.get().getId());

        try {
            log.info("Request : {}", data);
            Integer newId = service.TambahTransaksi(data);
            return ResponseEntity.ok(new CostumMessage(HttpStatus.OK, newId.toString()));
        } catch (Exception throwables) {
            throwables.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CostumMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Terjadi Kesalahan Server"));
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll(@RequestParam Integer idStore , @RequestParam Integer nomorKasir, @RequestParam Integer shift,@RequestParam Date tanggalAwal,@RequestParam Date tanggalAkhir){
        List<TransaksiDto.Transaksi> data = service.findAll(idStore, nomorKasir, shift, tanggalAwal, tanggalAkhir);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/findAllRinci/{idStore}/{idTransaksi}")
    public ResponseEntity<?> findAll(@PathVariable Integer idStore, @PathVariable Integer idTransaksi){
        List<TransaksiDto.TransaksiRinci> data = service.findAllRinci(idStore,idTransaksi);
        return ResponseEntity.ok(data);
    }

    @PostMapping("/changeTipePembayaran")
    public ResponseEntity<?> saveTransaksi(@RequestBody TransaksiDto.ChangeTipePembayaran data){
        try {
            log.info("Request : {}", data);
            service.changeTipePembayaran(data);
            return ResponseEntity.ok(new CostumMessage(HttpStatus.OK, "Data Terupdate"));
        } catch (Exception throwables) {
            throwables.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CostumMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Terjadi Kesalahan Server"));
        }
    }

}
