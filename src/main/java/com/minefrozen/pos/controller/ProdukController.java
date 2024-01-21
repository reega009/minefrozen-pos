package com.minefrozen.pos.controller;

import com.minefrozen.pos.dto.ProdukDto;
import com.minefrozen.pos.service.ProdukService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pos/product")
public class ProdukController {

    @Autowired
    private ProdukService service;

    @GetMapping("/findProdukByBarcode")
    public ResponseEntity<?> findProdukByBarcode(@RequestParam Integer barcode){
        Optional<ProdukDto.ProdukKasir> data = service.findProdukByBarcode(barcode);
        if (!data.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(data.get());
    }

    @GetMapping("findProdukBySearchName")
    public ResponseEntity<?> findProdukBySearchName(@RequestParam(required = false) String paramName){
        List<ProdukDto.ProdukKasir> data = service.findProdukBySearchName(paramName);
        return ResponseEntity.ok(data);
    }

}
