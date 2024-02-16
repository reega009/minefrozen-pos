package com.minefrozen.pos.controller;


import jakarta.servlet.ServletContextListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/pos/report")
@Slf4j
public class ReportController {

    @Autowired
    @Qualifier("dataSources")
    private DataSource dataSource;


    @PostMapping(value = "/struk-pembelian", headers = "Accept=*/*", produces = MediaType.APPLICATION_PDF_VALUE)
    @ResponseBody
    public ResponseEntity<?> reportStrukPembelian(
            @RequestParam Integer idTransaksi,
            @RequestParam Integer idStore,
            @RequestParam BigDecimal jumlahBayar,
            HttpServletResponse response,
            HttpServletRequest request) throws IOException, JRException, SQLException {

        log.info("STRUK PEMBELIAN DOWNLOAD");

        Map<String, Object> params = new HashMap<>();
        params.put("ID_TRANSAKSI", idTransaksi);
        params.put("ID_STORE", idStore);
        params.put("JUMLAH_BAYAR", jumlahBayar);
        params.put("SUBREPORT_DIR", Objects.requireNonNull(
                        ServletContextListener.class.getClassLoader().getResource("file/jasper"))
                .toString());

        log.info("PARAMETER => {}", params);

        try {

            log.info("TRY GET JASPER FILE");

//            File jasperFile = new File(basePath + "/Report_Kontrabon.jasper");
            InputStream jasperStream = this.getClass().getResourceAsStream("/file/jasper/Struk-Pembelian.jasper");


            log.info("JASPER FILE LOADED AND GENERATE JASPER PRINT");

//            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource.getConnection());

            response.setContentType("application/x-pdf");
            response.setHeader("Content-disposition", "inline; filename=" + "Struk-Belanja" + ".pdf");

            log.info("JASPER PRINT SUCCESS");

            final OutputStream outStream = response.getOutputStream();

            JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            log.info("GAGAL CETAK PDF");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
