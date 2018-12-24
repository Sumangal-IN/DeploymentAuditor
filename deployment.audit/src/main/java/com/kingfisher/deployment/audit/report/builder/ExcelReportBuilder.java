package com.kingfisher.deployment.audit.report.builder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
public class ExcelReportBuilder {

	public byte[] createEmptyExcel(String sheetName) throws IOException {
		Workbook workbook = new XSSFWorkbook();
		workbook.createSheet(sheetName);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		workbook.close();
		bos.close();
		return bos.toByteArray();
	}
}
