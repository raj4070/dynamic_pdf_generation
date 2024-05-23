package com.example.dynamicPdf.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dynamicPdf.model.InvoiceDetails;
import com.example.dynamicPdf.service.Services;

@RestController
@RequestMapping("/pdf")
public class Controller {
	
    @Autowired
    private Services pdfService;

    @PostMapping("/generate")
    public ResponseEntity<String> generatePdf(@RequestBody InvoiceDetails invoice) {
        try {
            String pdfFilePath = pdfService.generatePdf(invoice);
            return new ResponseEntity<>(pdfFilePath, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error generating PDF", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download")
    public ResponseEntity<FileSystemResource> downloadPdf(@RequestParam String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        FileSystemResource resource = new FileSystemResource(file);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);

}
}
