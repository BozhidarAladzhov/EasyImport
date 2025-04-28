package com.mytransport;

import com.mytransport.services.ExcelImportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EasyImportApplication {

	@Bean
	public CommandLineRunner importDataOnStartup(ExcelImportService excelImportService) {
		return args -> {
			excelImportService.importPricesFromExcel();
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(EasyImportApplication.class, args);
	}

}
