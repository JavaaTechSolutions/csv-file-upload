package com.jts.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jts.controller.entity.Files;
import com.jts.service.FileService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class FilesController {

	@Autowired
	private FileService fileService;

	@PostMapping("/uploadFilesIntoDB")
	public ResponseEntity<String> storeFilesIntoDB(@RequestParam MultipartFile file) throws IOException, CsvException {
		fileService.save(file);

		return ResponseEntity.status(HttpStatus.OK).body("Success");
	}

	@GetMapping("/exportCSV")
	public void exportCSV(HttpServletResponse response) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {
		String fileName = "employee-data.csv";

		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "");
		
		StatefulBeanToCsv<Files> writer = new StatefulBeanToCsvBuilder<Files>(response.getWriter())
				.withSeparator(CSVWriter.DEFAULT_SEPARATOR)
				.withOrderedResults(true)
				.build();
		
		writer.write(fileService.findAll());
	}

}
