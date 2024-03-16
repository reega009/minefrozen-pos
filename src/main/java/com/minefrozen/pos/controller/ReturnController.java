package com.minefrozen.pos.controller;

import com.minefrozen.pos.dto.CostumMessage;
import com.minefrozen.pos.dto.DiskonProdukDto;
import com.minefrozen.pos.dto.ReturnDto;
import com.minefrozen.pos.dto.UserDto;
import com.minefrozen.pos.service.ReturnService;
import com.minefrozen.pos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api-pos/return")
public class ReturnController {

    @Autowired
    private ReturnService service;

    @Autowired
    private UserService userService;

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
        // Get User Input
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<UserDto.UserLogin> pengguna = userService.findByUsername(username);
        if (!pengguna.isPresent()) {
            return ResponseEntity.badRequest().body(new CostumMessage(HttpStatus.BAD_REQUEST, "Pengguna Login Tidak Ditemukan"));
        }
        data.setIPgunRekam(pengguna.get().getId());

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
