package com.mytransport.services;

import com.mytransport.models.OceanFreightEntity;
import com.mytransport.repository.OceanFreightRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelImportService {

    private final OceanFreightRepository oceanFreightRepository;

    public ExcelImportService(OceanFreightRepository oceanFreightRepository) {
        this.oceanFreightRepository = oceanFreightRepository;
    }

    public void importPricesFromExcel (){

        try {
            InputStream inputStream = new ClassPathResource("data/OceanFreightDatabase.xlsx").getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            List<OceanFreightEntity> oceanFreightEntityList = new ArrayList<>();

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

                oceanFreightEntityList.add(new OceanFreightEntity(origin, destination, vehicleType, price));

            }
            oceanFreightRepository.deleteAll();
            oceanFreightRepository.saveAll(oceanFreightEntityList);
        } catch (Exception e) {
            e.printStackTrace();
        }




    }

}
