package com.kingfisher.deployment.audit.report.builder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kingfisher.deployment.audit.constant.ApplicationConstant;
import com.kingfisher.deployment.audit.data.model.Deployment;
import com.kingfisher.deployment.audit.report.model.ReportCell;

@Component
public class ExcelReportBuilder {

	@Autowired
	ExcelReportDataOrganizer excelReportDataOrganizer;

	/**
	 * Creates xlsx report from the given deployment information across several
	 * environment of different applications
	 * 
	 * @param referenceEnv
	 *            environment with which {@code reportingEnv} will be compared
	 * @param reportingEnv
	 *            a list of environment to be compared with {@code referenceEnv}
	 * @param reportData
	 *            a collective data of all latest deployment of different
	 *            applications in different environments
	 * @param sheetName
	 *            name of the sheet to modify in xlsx workbook
	 * @return {@code byte[]} representing the xlsx file
	 * @throws IOException
	 */
	public byte[] createReport(String referenceEnv, List<String> reportingEnv,
			Map<String, Map<String, List<Deployment>>> reportData, String sheetName) throws IOException {
		Workbook workbook = new XSSFWorkbook();
		ExcelReportStyleBuilder.addStyles(workbook);
		Sheet sheet = workbook.createSheet(sheetName);
		int rownum = 1;
		// create header at row 2
		rownum = createHeader(rownum, sheet, referenceEnv, reportingEnv);
		// create header at row 3
		rownum = createSubHeader(rownum, sheet, reportingEnv);
		// create header from row 4
		createBody(rownum, sheet, referenceEnv, reportData);
		// auto-resize width of all cells according to its content
		for (int col = 0; col < 7 + (3 * reportingEnv.size()); col++)
			sheet.autoSizeColumn(col);
		// returns the workbook as byte array
		return workbookToByte(workbook);
	}

	/**
	 * Creates the heading of the report
	 * 
	 * @param rownum
	 *            row number from which the heading will be plotted
	 * @param sheet
	 *            name of the sheet to modify in xlsx workbook
	 * @param referenceEnv
	 *            environment with which {@code reportingEnv} will be compared
	 * @param reportingEnv
	 *            a list of environment to be compared with {@code referenceEnv}
	 * @return the row number after the end of the heading
	 */
	private int createHeader(int rownum, Sheet sheet, String referenceEnv, List<String> reportingEnv) {
		Row row = ExcelReportStyleBuilder.formatRow(sheet.createRow(rownum), "header");
		// skipping first 3 cells
		int cellnum = 3;
		Cell cell = row.createCell(cellnum);
		ExcelReportStyleBuilder.setValueWithFormatting(cell, referenceEnv, ExcelReportStyleBuilder.headerStyle);
		sheet.addMergedRegion(new CellRangeAddress(rownum, rownum, cellnum, cellnum + 2));
		cellnum += 3;
		for (String env : reportingEnv) {
			cell = row.createCell(cellnum);
			ExcelReportStyleBuilder.setValueWithFormatting(cell, env, ExcelReportStyleBuilder.headerStyle);
			sheet.addMergedRegion(new CellRangeAddress(rownum, rownum, cellnum, cellnum + 2));
			cellnum += 3;
		}
		return ++rownum;
	}

	/**
	 * Creates the sub-heading of the report
	 * 
	 * @param rownum
	 *            row number from which the sub-heading will be plotted
	 * @param sheet
	 *            name of the sheet to modify in xlsx workbook
	 * @param reportingEnv
	 *            a list of environment to be compared with {@code referenceEnv}
	 * @return the row number after the end of the sub-heading
	 */
	private int createSubHeader(int rownum, Sheet sheet, List<String> reportingEnv) {
		Row row = ExcelReportStyleBuilder.formatRow(sheet.createRow(rownum), "subHeader");
		// skipping first 1 cell
		int cellnum = 1;
		Cell cell = row.createCell(cellnum++);
		ExcelReportStyleBuilder.setValueWithFormatting(cell, ApplicationConstant.REPORT_STRING_APPLICATION_NAME,
				ExcelReportStyleBuilder.subHeaderStyle);
		cell = row.createCell(cellnum++);
		ExcelReportStyleBuilder.setValueWithFormatting(cell, ApplicationConstant.REPORT_STRING_STATUS,
				ExcelReportStyleBuilder.subHeaderStyle);
		for (int i = 0; i <= reportingEnv.size(); i++) {
			cell = row.createCell(cellnum++);
			ExcelReportStyleBuilder.setValueWithFormatting(cell, ApplicationConstant.REPORT_STRING_INSTANCE,
					ExcelReportStyleBuilder.subHeaderStyle);
			cell = row.createCell(cellnum++);
			ExcelReportStyleBuilder.setValueWithFormatting(cell, ApplicationConstant.REPORT_STRING_EG,
					ExcelReportStyleBuilder.subHeaderStyle);
			cell = row.createCell(cellnum++);
			ExcelReportStyleBuilder.setValueWithFormatting(cell, ApplicationConstant.REPORT_STRING_VERSION,
					ExcelReportStyleBuilder.subHeaderStyle);
		}
		return ++rownum;
	}

	/**
	 * Creates the body of the report
	 * 
	 * @param rownum
	 *            row number from which the body will be plotted
	 * @param sheet
	 *            name of the sheet in workbook
	 * @param referenceEnv
	 *            name of the environment which will be compared with others
	 * @param reportData
	 *            a collective data of all latest deployment of different
	 *            applications in different environments
	 * @return the row number after the end of the body
	 */
	private int createBody(int rownum, Sheet sheet, String referenceEnv,
			Map<String, Map<String, List<Deployment>>> reportData) {

		for (Entry<String, Map<String, List<Deployment>>> rowDataPerApplication : reportData.entrySet()) {
			String applicationName = rowDataPerApplication.getKey();
			Map<String, List<Deployment>> latestDeploymentsAllInstance = rowDataPerApplication.getValue();

			List<ReportCell[]> organizedReportRows = excelReportDataOrganizer.organize(referenceEnv,
					latestDeploymentsAllInstance);

			rownum = createRowsForApplication(applicationName, rownum, sheet, organizedReportRows);
		}
		return rownum;
	}

	private int createRowsForApplication(String applicationName, int currentRow, Sheet sheet,
			List<ReportCell[]> organizedReportRows) {
		boolean printAppNameStatus = true;
		for (ReportCell[] organizedReportRow : organizedReportRows) {
			Row row = ExcelReportStyleBuilder.formatRow(sheet.createRow(currentRow), "body");
			int cellnum = 0;
			if (printAppNameStatus) {
				Cell cell = row.createCell(++cellnum);
				ExcelReportStyleBuilder.setValueWithFormatting(cell, applicationName,
						ExcelReportStyleBuilder.bodyStyle);
				cell = row.createCell(++cellnum);
				ExcelReportStyleBuilder.setValueWithFormatting(cell, "û",
						ExcelReportStyleBuilder.bodyStyleSpecialCharacterRed);
				ExcelReportStyleBuilder.setValueWithFormatting(cell, "ü",
						ExcelReportStyleBuilder.bodyStyleSpecialCharacterGreen);
				printAppNameStatus = false;
			} else {
				Cell cell = row.createCell(++cellnum);
				ExcelReportStyleBuilder.setValueWithFormatting(cell, "", ExcelReportStyleBuilder.bodyStyle);
				cell = row.createCell(++cellnum);
				ExcelReportStyleBuilder.setValueWithFormatting(cell, "", ExcelReportStyleBuilder.bodyStyle);
			}
			createRowForApplication(organizedReportRow, cellnum, row);
			currentRow++;
		}
		return currentRow;
	}

	/**
	 * 
	 * @param rownum
	 * @param rowDataPerApplicationPerInstance
	 * @param cellnum
	 * @param row
	 * @return
	 */
	private void createRowForApplication(ReportCell[] organizedReportRow, int cellnum, Row row) {
		/* there are data for current row */
		Cell cell = null;
		for (int i = 0; i < organizedReportRow.length; i++) {
			cell = row.createCell(++cellnum);
			ExcelReportStyleBuilder.setValueWithFormatting(cell, organizedReportRow[i].getValue(),
					organizedReportRow[i].isAmbiguous() ? ExcelReportStyleBuilder.bodyStyleAlert
							: ExcelReportStyleBuilder.bodyStyle);
		}
	}

	/**
	 * Converts a xlsx workbook into {@code byte[]}
	 * 
	 * @param workbook
	 *            xlsx workbook to be converted
	 * @return {@code byte[]} representation of the xlsx workbook
	 * @throws IOException
	 */
	private byte[] workbookToByte(Workbook workbook) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		workbook.close();
		bos.close();
		return bos.toByteArray();
	}
}