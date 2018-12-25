package com.kingfisher.deployment.audit.report.builder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
public class ExcelReportBuilder {

	public byte[] createReportWithEnvData(String referenceEnv, Map<String, String[]> reportDataReferenceEnv, List<String> reportingEnv, List<Map<String, String[]>> reportDataReportingEnv, String sheetName) throws IOException {

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet(sheetName);
		createHeader(sheet, referenceEnv, reportingEnv);
		createSubHeader(sheet, reportingEnv);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		workbook.close();
		bos.close();
		return bos.toByteArray();
	}

	private void createSubHeader(Sheet sheet, List<String> reportingEnv) {
		Row row = sheet.createRow(2);
		int cellnum = 2;
		Cell cell = row.createCell(cellnum++);
		cell.setCellValue("Application Name");
		cell = row.createCell(cellnum++);
		for (int i = 0; i <= reportingEnv.size(); i++) {
			cell = row.createCell(cellnum++);
			cell.setCellValue("Instance");
			cell = row.createCell(cellnum++);
			cell.setCellValue("EG");
			cell = row.createCell(cellnum++);
			cell.setCellValue("Version");
		}
	}

	private void createHeader(Sheet sheet, String referenceEnv, List<String> reportingEnv) {
		Row row = sheet.createRow(1);
		int cellnum = 4;
		Cell cell = row.createCell(cellnum);
		cell.setCellValue(referenceEnv);
		cellnum += 3;
		for (String env : reportingEnv) {
			cell = row.createCell(cellnum);
			cell.setCellValue(env);
			cellnum += 3;
		}

	}
}
