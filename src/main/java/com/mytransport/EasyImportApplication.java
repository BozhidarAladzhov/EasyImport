package com.mytransport;

import com.mytransport.services.ExcelImports.DomesticRotterdamXLSImportService;
import com.mytransport.services.ExcelImports.OceanFreightXLSImportService;
import com.mytransport.services.ExcelImports.TerminalHandlingXLSImportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EasyImportApplication {

	@Bean
	public CommandLineRunner importDataOnStartup(OceanFreightXLSImportService oceanFreightXLSImportService,
												 TerminalHandlingXLSImportService terminalHandlingXLSImportService,
												 DomesticRotterdamXLSImportService domesticRotterdamXLSImportService) {
		return args -> {
			oceanFreightXLSImportService.importPricesFromExcel();
			terminalHandlingXLSImportService.importPricesFromExcel();
			domesticRotterdamXLSImportService.importPricesFromExcel();
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(EasyImportApplication.class, args);
	}

}
