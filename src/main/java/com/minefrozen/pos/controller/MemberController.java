package com.minefrozen.pos.controller;

import com.minefrozen.pos.dto.CostumMessage;
import com.minefrozen.pos.dto.MemberDto;
import com.minefrozen.pos.dto.UserDto;
import com.minefrozen.pos.service.MemberService;
import com.minefrozen.pos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api-pos/member")
public class MemberController {

    @Autowired
    private MemberService service;

    @Autowired
    private UserService userService;

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll(@RequestParam Integer idStore){
        List<MemberDto.Member> data = service.findAll(idStore);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam Integer id, @RequestParam Integer idStore){
        Optional<MemberDto.Member> data = service.findById(id, idStore);
        if (!data.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(data.get());
    }

    @GetMapping("/findByNameOrKodeMember")
    public ResponseEntity<?> findByNameOrKodeMember(@RequestParam Integer idStore, @RequestParam String namaMember, @RequestParam String kodeMember){
        Optional<MemberDto.Member> data = service.findByNameOrKodeMember(idStore, namaMember, kodeMember);
        if (!data.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(data.get());
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody MemberDto.Member data){
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

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody MemberDto.Member data){
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
