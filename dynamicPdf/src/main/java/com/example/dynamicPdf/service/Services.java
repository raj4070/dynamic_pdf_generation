package com.example.dynamicPdf.service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;

import java.util.UUID;


import org.springframework.stereotype.Service;

import com.example.dynamicPdf.model.InvoiceDetails;
import com.example.dynamicPdf.model.Item;

@Service
public class Services {
	
	private static final String STORAGE_DIR = "pdf-storage/";

    public Services() {
        try {
            Files.createDirectories(Paths.get(STORAGE_DIR));
        } catch (Exception e) {
        	e.getMessage();
        }
    }

    public String generatePdf(InvoiceDetails invoice) throws Exception {
    	String pdfFileName = STORAGE_DIR + UUID.randomUUID() + ".pdf";
        try (PdfWriter writer = new PdfWriter(pdfFileName);
             Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer))) {
        	
        	PdfFont pdfFont = PdfFontFactory.createFont(StandardFonts.COURIER_BOLD);
        	document.setFont(pdfFont);

            float[] sellerBuyerColumnWidths = {280F, 280F};
            Table sellerBuyerTable = new Table(sellerBuyerColumnWidths);


            // Seller Cell
            Cell sellerCell = new Cell();

            Paragraph sellerPara = new Paragraph().setPadding(30);
            sellerPara.add("Seller:");
            sellerPara.add("\n" +invoice.getSeller());
            sellerPara.add("\n" +invoice.getSellerAddress());
            sellerPara.add("\nGSTIN: " + invoice.getSellerGstin());
            sellerCell.add(sellerPara);

            sellerBuyerTable.addCell(sellerCell);

            // Buyer Cell
            Cell buyerCell = new Cell();

            Paragraph buyerPara = new Paragraph().setPadding(30);
      


            buyerPara.add("Buyer:");
            buyerPara.add("\n" +invoice.getBuyer());
            buyerPara.add("\n" +invoice.getBuyerAddress());
            buyerPara.add("\nGSTIN: " + invoice.getBuyerGstin());
            buyerCell.add(buyerPara);
            sellerBuyerTable.addCell(buyerCell);
            document.add(sellerBuyerTable);

            float[] itemColumnWidths = {200F, 120F, 120F, 120F};
            Table itemTable = new Table(itemColumnWidths);

            itemTable.addCell(new Cell().add(new Paragraph("Item")).setTextAlignment(TextAlignment.CENTER));
            itemTable.addCell(new Cell().add(new Paragraph("Quantity")).setTextAlignment(TextAlignment.CENTER));
            itemTable.addCell(new Cell().add(new Paragraph("Rate")).setTextAlignment(TextAlignment.CENTER));
            itemTable.addCell(new Cell().add(new Paragraph("Amount")).setTextAlignment(TextAlignment.CENTER));

            for (Item item : invoice.getItems()) {
                itemTable.addCell(new Cell().add(new Paragraph(item.getName())).setTextAlignment(TextAlignment.CENTER));
                itemTable.addCell(new Cell().add(new Paragraph(item.getQuantity())).setTextAlignment(TextAlignment.CENTER));
                itemTable.addCell(new Cell().add(new Paragraph(String.valueOf(item.getRate()))).setTextAlignment(TextAlignment.CENTER));
                itemTable.addCell(new Cell().add(new Paragraph(String.valueOf(item.getAmount()))).setTextAlignment(TextAlignment.CENTER));
            }

            document.add(itemTable);

        } catch (Exception e) {
        	e.getMessage();           
        } 
        return pdfFileName;

}
}
