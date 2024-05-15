package com.minefrozen.pos.controller;

import com.minefrozen.pos.dto.CostumMessage;
import com.minefrozen.pos.dto.DiskonProdukDto;
import com.minefrozen.pos.dto.ProdukDto;
import com.minefrozen.pos.service.ProdukService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api-pos/product")
public class ProdukController {

    @Autowired
    private ProdukService service;

    @GetMapping("/findProdukByBarcode")
    public ResponseEntity<?> findProdukByBarcode(@RequestParam String barcode){
        System.out.println("-----------BARCODE -----------" + barcode);
        Optional<ProdukDto.ProdukKasir> data = service.findProdukByBarcode(barcode);
        if (!data.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(data.get());
    }

    @GetMapping("/findProdukBySearchName")
    public ResponseEntity<?> findProdukBySearchName(@RequestParam String paramName){
        Optional<ProdukDto.ProdukKasir> data = service.findProdukBySearchName(paramName);
        if (!data.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(data.get());
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll(){
        List<ProdukDto.Produk> data = service.findAll();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id){
        Optional<ProdukDto.Produk> data = service.findById(id);
        if (!data.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(data.get());
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody ProdukDto.Produk data){
        try {
            service.save(data);
            return ResponseEntity.ok(new CostumMessage(HttpStatus.OK, "Data Tersimpan"));
        } catch (Exception throwables) {
            throwables.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CostumMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Terjadi Kesalahan Server"));
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody ProdukDto.Produk data){
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

    @PostMapping("/matchingDataWithServer")
    public ResponseEntity<?> matchingDataWithServer(){
        try {
            service.matchingDataWithServer();
            return ResponseEntity.ok(new CostumMessage(HttpStatus.OK, "Data Terupdate"));
        } catch (Exception throwables) {
            throwables.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CostumMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Terjadi Kesalahan Server"));
        }
    }

}
