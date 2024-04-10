package com.benkitoucoders.ecommerce.services.pdfs;

import com.benkitoucoders.ecommerce.dtos.ClientOrderDetailsDto;
import com.benkitoucoders.ecommerce.dtos.ClientOrderDto;
import com.benkitoucoders.ecommerce.dtos.SaleDetailsDto;
import com.benkitoucoders.ecommerce.dtos.SaleDto;
import com.benkitoucoders.ecommerce.dtos.EmailDetails;
import com.benkitoucoders.ecommerce.services.inter.EmailService;
import com.benkitoucoders.ecommerce.services.inter.ClientOrderDetailsService;
import com.benkitoucoders.ecommerce.services.inter.SaleDetailsService;
import com.benkitoucoders.ecommerce.services.inter.SaleService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeliveredOrderStatement {
    private final ClientOrderDetailsService clientOrderDetailsService;
    private final SaleService saleService;
    private final SaleDetailsService saleDetailsService;
    private final EmailService emailService;

    private static final String FILE = "C:\\Users\\PC\\Desktop\\Projects\\1-Ecommerce\\deliveredOrderStatement.pdf";

    // In our app we handle two types of order, when the client is registered in our app, or it is just a sale the customer is not registered
    // So if isClientOrder we will make search in ClientOrderService else SaleService
    public void generateDeliveredOrderStatement(Long orderId, ClientOrderDto clientOrderDto, boolean isClientOrder) throws FileNotFoundException, DocumentException {
        if (isClientOrder) {
            List<ClientOrderDetailsDto> clientOrderDetailsto = clientOrderDetailsService.getClientOrderDetailsByQuery(clientOrderDto.getId());

            LocalDate start = LocalDate.parse(LocalDate.now().toString(), DateTimeFormatter.ISO_DATE);

            Rectangle statementSize = new Rectangle(PageSize.A4);
            Document document = new Document(statementSize);
            log.info("Setting size of document");
            OutputStream outputStream = new FileOutputStream(FILE);
            PdfWriter.getInstance(document, outputStream);
            document.open();

            PdfPTable bankInfoTable = new PdfPTable(1);
            PdfPCell bankName = new PdfPCell(new Phrase("BOKEITO SHOP"));
            bankName.setBorder(0);
            bankName.setBackgroundColor(BaseColor.GREEN);
            bankName.setPadding(20f);

            PdfPCell bankAddress = new PdfPCell(new Phrase("72, Some Address, Casablanca Maroc"));
            bankAddress.setBorder(0);
            bankInfoTable.addCell(bankName);
            bankInfoTable.addCell(bankAddress);

            PdfPTable statementInfo = new PdfPTable(2);
            PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date: " + start));
            customerInfo.setBorder(0);
            PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
            statement.setBorder(0);
            PdfPCell stopDate = new PdfPCell(new Phrase("End Date: " + start));
            stopDate.setBorder(0);
            PdfPCell name = new PdfPCell(new Phrase("Customer Name: " + "customerName"));
            name.setBorder(0);
            PdfPCell space = new PdfPCell();
            PdfPCell address = new PdfPCell(new Phrase("Customer Address: " + "user.getAddress()"));
            address.setBorder(0);


            PdfPTable transactionsTable = new PdfPTable(4);
            PdfPCell date = new PdfPCell(new Phrase("DATE"));
            date.setBackgroundColor(BaseColor.GREEN);
            date.setBorder(0);

            PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
            transactionType.setBackgroundColor(BaseColor.GREEN);
            transactionType.setBorder(0);

            PdfPCell transactionAmount = new PdfPCell(new Phrase("TRANSACTION Amount"));
            transactionAmount.setBackgroundColor(BaseColor.GREEN);
            transactionAmount.setBorder(0);

            PdfPCell status = new PdfPCell(new Phrase("Status"));
            status.setBackgroundColor(BaseColor.GREEN);
            status.setBorder(0);

            transactionsTable.addCell(date);
            transactionsTable.addCell(transactionType);
            transactionsTable.addCell(transactionAmount);
            transactionsTable.addCell(status);

            clientOrderDetailsto.forEach(clientOrderDetailsDto -> {
                transactionsTable.addCell(new Phrase(clientOrderDetailsDto.getProductName()));
                transactionsTable.addCell(new Phrase(clientOrderDetailsDto.getClientOrderId()));
                transactionsTable.addCell(new Phrase(clientOrderDetailsDto.getQuantity().toString()));
                transactionsTable.addCell(new Phrase(clientOrderDetailsDto.getProductName()));
            });

            statementInfo.addCell(customerInfo);
            statementInfo.addCell(statement);
            statementInfo.addCell(String.valueOf(start));
            statementInfo.addCell(name);
            statementInfo.addCell(space);
            statementInfo.addCell(address);

            document.add(bankInfoTable);
            document.add(statementInfo);
            document.add(transactionsTable);

            document.close();

            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient("rachidbenkitou1@gmail.com")
                    .subject("STATEMENT OF ACCOUNT")
                    .messageBody("Kindly find your requested statement attached!")
                    .attachment(FILE)
                    .build();

            emailService.sendEmailWithAttachment(emailDetails);
        } else {
            SaleDto saleDto = saleService.findsalesById(orderId);
            List<SaleDetailsDto> saleDetailsDtos = saleDetailsService.findAllSaleDetailsById(saleDto.getId());
        }


    }


}
