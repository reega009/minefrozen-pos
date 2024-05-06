package com.minefrozen.pos.controller;

import com.minefrozen.pos.dto.CostumMessage;
import com.minefrozen.pos.dto.UserDto;
import com.minefrozen.pos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/user")
public class UserController {


    @Autowired
    private UserService service;


    @GetMapping("/findAll")
    public ResponseEntity<?> findAll(){
        List<UserDto.User> data = service.findAll();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/findByUsername/{username}")
    public ResponseEntity<?> findByUsernameg(@PathVariable String username){
        Optional<UserDto.UserLogin> data = service.findByUsername(username);
        if (!data.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(data.get());
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id){
        Optional<UserDto.User> data = service.findById(id);
        if (!data.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(data.get());
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody UserDto.User data, Principal principal){
        Optional<UserDto.UserLogin> pengguna = service.findByUsername(principal.getName());
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

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody UserDto.User data, Principal principal){
        Optional<UserDto.UserLogin> pengguna = service.findByUsername(principal.getName());
        if (!pengguna.isPresent()) {
            return ResponseEntity.badRequest().body(new CostumMessage(HttpStatus.BAD_REQUEST, "Pengguna Login Tidak Ditemukan"));
        }
        data.setIPgunUbah(pengguna.get().getId());

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
