package com.mytransport;

import com.mytransport.services.DomesticRotterdamExcelImportService;
import com.mytransport.services.OceanFreightExcelImportService;
import com.mytransport.services.TerminalHandlingExcelImportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EasyImportApplication {

	@Bean
	public CommandLineRunner importDataOnStartup(OceanFreightExcelImportService oceanFreightExcelImportService,
												 TerminalHandlingExcelImportService terminalHandlingExcelImportService,
												 DomesticRotterdamExcelImportService domesticRotterdamExcelImportService) {
		return args -> {
			oceanFreightExcelImportService.importPricesFromExcel();
			terminalHandlingExcelImportService.importPricesFromExcel();
			domesticRotterdamExcelImportService.importPricesFromExcel();
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(EasyImportApplication.class, args);
	}

}
