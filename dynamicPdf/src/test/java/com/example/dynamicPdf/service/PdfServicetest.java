package com.example.dynamicPdf.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.dynamicPdf.model.InvoiceDetails;
import com.example.dynamicPdf.model.Item;
import com.itextpdf.kernel.pdf.PdfWriter;

@ExtendWith(MockitoExtension.class)
public class PdfServicetest {
	@InjectMocks
	Services pdfservice;
	
	@BeforeEach
    void setUp() {
		pdfservice = new Services();
    }
	@Test
	void pdf_generate_success() throws Exception {
		InvoiceDetails invoice = new InvoiceDetails();
        invoice.setSeller("XYZ Pvt. Ltd.");
        invoice.setSellerAddress("New Delhi, India");
        invoice.setSellerGstin("29AABBCCDD121ZD");
        invoice.setBuyer("Vedant Computers");
        invoice.setBuyerAddress("New Delhi, India");
        invoice.setBuyerGstin("29AABBCCDD131ZD");

        List<Item> itemlist = new ArrayList<>();
        Item item =new Item();
        item.setName("Product 1");
        item.setQuantity("12 Nos");
        item.setRate(123.00);
        item.setAmount(1476.00);
        itemlist.add(item);
        invoice.setItems(itemlist);
        PdfWriter pdfWriter = mock(PdfWriter.class);
        String pdfPath = pdfservice.generatePdf(invoice);
   
        File pdfFile = new File(pdfPath);
        assertTrue(pdfFile.exists());
        
	}

}
