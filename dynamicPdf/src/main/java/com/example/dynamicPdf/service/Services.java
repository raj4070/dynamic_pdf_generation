package com.example.dynamicPdf.service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.example.dynamicPdf.model.InvoiceDetails;
import com.example.dynamicPdf.model.Item;

@Service
public class Services {
	
	private static final String STORAGE_DIR = "pdf-storage/";
    private static final Logger LOGGER = Logger.getLogger(Services.class.getName());

    public Services() {
        try {
            Files.createDirectories(Paths.get(STORAGE_DIR));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to create storage directory", e);
        }
    }

    public String generatePdf(InvoiceDetails invoice) throws FileNotFoundException {
    	String pdfFileName = STORAGE_DIR + UUID.randomUUID() + ".pdf";
        try (PdfWriter writer = new PdfWriter(pdfFileName);
             Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer))) {

            float[] sellerBuyerColumnWidths = {250F, 250F};
            Table sellerBuyerTable = new Table(sellerBuyerColumnWidths);
            sellerBuyerTable.setHorizontalAlignment(HorizontalAlignment.CENTER);


            // Seller Cell
            Cell sellerCell = new Cell();
            sellerCell.setTextAlignment(TextAlignment.CENTER);
//            sellerCell.setVerticalAlignment(com.itextpdf.layout.property.VerticalAlignment.MIDDLE);

            Paragraph sellerPara = new Paragraph();
            sellerPara.setTextAlignment(TextAlignment.LEFT);
            sellerPara.add("Seller:");
            sellerPara.add("\n" +invoice.getSeller());
            sellerPara.add("\n" +invoice.getSellerAddress());
            sellerPara.add("\nGSTIN: " + invoice.getSellerGstin());
            sellerCell.add(sellerPara);

            sellerBuyerTable.addCell(sellerCell);

            // Buyer Cell
            Cell buyerCell = new Cell();
            buyerCell.setTextAlignment(TextAlignment.CENTER);
//            buyerCell.setVerticalAlignment(com.itextpdf.layout.property.VerticalAlignment.MIDDLE);

            Paragraph buyerPara = new Paragraph();
            buyerPara.setHorizontalAlignment(HorizontalAlignment.CENTER);
            buyerPara.setVerticalAlignment(com.itextpdf.layout.property.VerticalAlignment.MIDDLE);
            buyerPara.setTextAlignment(TextAlignment.LEFT);


            buyerPara.add("Buyer:");
            buyerPara.add("\n" +invoice.getBuyer());
            buyerPara.add("\n" +invoice.getBuyerAddress());
            buyerPara.add("\nGSTIN: " + invoice.getBuyerGstin());
            buyerCell.add(buyerPara);
            sellerBuyerTable.addCell(buyerCell);
            document.add(sellerBuyerTable);

            float[] itemColumnWidths = {200F, 100F, 100F, 100F};
            Table itemTable = new Table(itemColumnWidths);
            itemTable.setHorizontalAlignment(HorizontalAlignment.CENTER);

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

        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "PDF file not found", e);
            return null;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error generating PDF", e);
            return null;
        }
        return pdfFileName;

}
}
