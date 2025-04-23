package com.mytransport.services;

import com.mytransport.models.TransportPriceEntity;
import com.mytransport.repository.TransportPriceRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelImportService {

    private final TransportPriceRepository transportPriceRepository;

    public ExcelImportService(TransportPriceRepository transportPriceRepository) {
        this.transportPriceRepository = transportPriceRepository;
    }

    public void importPricesFromExcel (){

        try {
            InputStream inputStream = new ClassPathResource("data/DatabasePrices.xlsx").getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            List<TransportPriceEntity> prices = new ArrayList<>();

            String currentRoute = null;


            for (int i = 1; i<= sheet.getLastRowNum(); i++){
                Row row = sheet.getRow(i);

                if (row == null) continue;

                Cell polCell = row.getCell(0);
                Cell podCell = row.getCell(1);
                Cell vehicleCell = row.getCell(2);
                Cell costCell = row.getCell(3);

                if (polCell == null || podCell == null || vehicleCell == null || costCell == null) continue;

                String origin = polCell.getStringCellValue().trim();
                String destination = podCell.getStringCellValue().trim();
                String vehicleType = vehicleCell.getStringCellValue().trim();

                double price;
                if (costCell.getCellType() == CellType.NUMERIC) {
                    price = costCell.getNumericCellValue();
                } else {
                    String raw = costCell.getStringCellValue().replace("$", "").replace(",", ".").replace(" ", "");
                    price = Double.parseDouble(raw);
                }

                prices.add(new TransportPriceEntity(origin, destination, vehicleType, price));

            }
            transportPriceRepository.deleteAll();
            transportPriceRepository.saveAll(prices);
        } catch (Exception e) {
            e.printStackTrace();
        }




    }

}
