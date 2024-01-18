package com.practice.cafesystem.serviceImpl;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.practice.cafesystem.constants.CafeConstants;
import com.practice.cafesystem.dao.BillDAO;
import com.practice.cafesystem.jwt.JwtMyFilter;
import com.practice.cafesystem.pojo.Bill;
import com.practice.cafesystem.service.BillService;
import com.practice.cafesystem.utils.CafeUtils;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    BillDAO billDAO;

    @Autowired
    JwtMyFilter jwtMyFilter;

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        try {
            String fileName;
            if (validateRequestMap(requestMap)) {
                if (requestMap.containsKey("isGenerated") && !Boolean.parseBoolean(requestMap.get("isGenerated") + "")) {
                    fileName = requestMap.get("uuid") + "";
                } else {
                    fileName = CafeUtils.getUUID();
                    requestMap.put("uuid", fileName);
                    insertBill(requestMap);
                }
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(CafeConstants.STORE_LOCATION + "\\" + fileName + ".pdf"));
                // --- Draw outside border --------------------
                document.open();
                setRectangleInPdf(document);

                // --- Add header -----------------------------
                Paragraph chunk = new Paragraph("Cafe Management System", getFont("header"));
                chunk.setAlignment(Element.ALIGN_CENTER);
                document.add(chunk);

                // ----
                String data = "Name: " + requestMap.get("name") + "\nContact Number: " + requestMap.get("contactNumber") + "\nEmail: " + requestMap.get("email") + "\nPayment Method: " + requestMap.get("paymentMethod");
                Paragraph paragraph = new Paragraph(data + "\n\n", getFont("data"));
                document.add(paragraph);

                // --- Insert main Table ( table header + data ) ---
                PdfPTable pdfPTable = new PdfPTable(5);
                pdfPTable.setWidthPercentage(100);
                addTableHeader(pdfPTable);
                JSONArray jsonArray = CafeUtils.getJsonArrayFromString((String) requestMap.get("productDetails"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    addRows(pdfPTable, CafeUtils.getMapFromJSON(jsonArray.getString(i)));
                }
                document.add(pdfPTable);

                // --- Add footer and close PDF --------------------
                Paragraph footer = new Paragraph("Total:" + requestMap.get("totalAmount") + "\nThank you for visiting. Please visit again !!!  ", getFont("data"));
                document.add(footer);

                document.add( new Paragraph("Generated at: "+ new Date() ) );
                document.close();
                // --------------------------------------------
                return CafeUtils.getResponseEntity("uuid:" + fileName , HttpStatus.OK);
            }
            return CafeUtils.getResponseEntity("Required Data not found", HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        try {
            List<Bill> bills = new ArrayList<>();
            if (jwtMyFilter.isAdmin()) {
                bills = billDAO.getAllBills();
            } else {
                bills = billDAO.getBillsByUserName(jwtMyFilter.getCurrentUser());
            }
            return new ResponseEntity<>(bills, HttpStatus.OK);
        } catch (Exception exce) {
            exce.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
        try {
            byte[] bytes;

            if (requestMap.containsKey("uuid") || validateRequestMap(requestMap)) {
                String filePath = CafeConstants.STORE_LOCATION + "\\" + requestMap.get("uuid") + ".pdf";

                if (!CafeUtils.fileExist(filePath)) {
                    requestMap.put("isGenerated", false);
                    generateReport(requestMap);
                }
                bytes = Files.readAllBytes(Paths.get(filePath));
                return new ResponseEntity<>(bytes, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception exce) {
            exce.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteBill(Integer id) {
        try {
            Optional bill = billDAO.findById(id);
            if (bill.isPresent()) {
                billDAO.deleteById(id);
                return CafeUtils.getResponseEntity("Bill REMOVED successfully", HttpStatus.OK);
            }
            return CafeUtils.getResponseEntity("Bill DOESNT exist", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception exl) {
            exl.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void addRows(PdfPTable pdfPTable, Map<String, Object> mapFromJSON) {
        pdfPTable.addCell(""+ mapFromJSON.get("name"));
        pdfPTable.addCell(""+ mapFromJSON.get("category"));
        pdfPTable.addCell(""+ mapFromJSON.get("quantity"));
        pdfPTable.addCell(""+ mapFromJSON.get("price"));
        pdfPTable.addCell(""+ mapFromJSON.get("total"));
    }

    private void addTableHeader(PdfPTable pdfPTable) {
        Stream.of("Name", "Category", "Quantity", "Price", "Sub Total")
                .forEach(columnTtile -> {

                    PdfPCell header = new PdfPCell();
                    header.setBorderColor(BaseColor.BLACK);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTtile));
                    header.setBackgroundColor(BaseColor.YELLOW);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_CENTER);

                    pdfPTable.addCell(header);
                });
    }

    private Font getFont(String type) {
        Font font = new Font();
        switch (type) {
            case "header":
                font = FontFactory.getFont(FontFactory.COURIER_OBLIQUE, 25, BaseColor.BLACK);
            case "data":
                font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, BaseColor.BLACK);
        }
        return font;
    }

    private void setRectangleInPdf(Document document) throws DocumentException {
        Rectangle rectangle = new Rectangle(577, 825, 18, 15);
        rectangle.enableBorderSide(1);
        rectangle.enableBorderSide(2);
        rectangle.enableBorderSide(4);
        rectangle.enableBorderSide(8);
        rectangle.setBackgroundColor(BaseColor.WHITE);
        rectangle.setBorderWidth(1);
        document.add(rectangle);
    }

    private void insertBill(Map<String, Object> requestMap) {
        try {
            Bill bill = new Bill();
            bill.setUuid((String) requestMap.get("uuid"));
            bill.setName((String) requestMap.get("name"));
            bill.setEmail((String) requestMap.get("email"));
            bill.setContactNumber((String) requestMap.get("contactNumber"));
            bill.setPaymentMethod((String) requestMap.get("paymentMethod"));
            bill.setTotal((Integer) requestMap.get("totalAmount"));
            bill.setProductDetail((String) requestMap.get("productDetails"));
            bill.setCreatedBy(jwtMyFilter.getCurrentUser());

            billDAO.save(bill);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean validateRequestMap(Map<String, Object> requestMap) {
        return requestMap.containsKey("name") &&
                requestMap.containsKey("contactNumber") &&
                requestMap.containsKey("email") &&
                requestMap.containsKey("paymentMethod") &&
                requestMap.containsKey("productDetails") &&
                requestMap.containsKey("totalAmount");
    }
}
