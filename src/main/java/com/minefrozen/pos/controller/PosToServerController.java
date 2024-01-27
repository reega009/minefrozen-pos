package com.minefrozen.pos.controller;

import com.minefrozen.pos.dto.CostumMessage;
import com.minefrozen.pos.dto.TransaksiDto;
import com.minefrozen.pos.service.PosToServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pos/pos-to-server")
public class PosToServerController {

    @Autowired
    private PosToServerService service;

    @PostMapping("/sendDataBackupToServer")
    public ResponseEntity<?> sendDataBackupToServer(){
        try {
            service.sendDataBackupToServer();
            return ResponseEntity.ok(new CostumMessage(HttpStatus.OK, "Data Tersimpan"));
        } catch (Exception throwables) {
            throwables.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CostumMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Terjadi Kesalahan Server"));
        }
    }

}
