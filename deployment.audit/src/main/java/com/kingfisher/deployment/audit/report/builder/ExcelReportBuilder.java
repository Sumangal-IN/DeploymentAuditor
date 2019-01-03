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
import org.springframework.stereotype.Component;

import com.kingfisher.deployment.audit.constant.ApplicationConstant;

@Component
public class ExcelReportBuilder {

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
	public byte[] createReport(String referenceEnv, List<String> reportingEnv, Map<String, Map<String, List<String[][]>>> reportData, String sheetName) throws IOException {
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
		ExcelReportStyleBuilder.setValueWithFormatting(cell, ApplicationConstant.REPORT_STRING_APPLICATION_NAME, ExcelReportStyleBuilder.subHeaderStyle);
		cell = row.createCell(cellnum++);
		ExcelReportStyleBuilder.setValueWithFormatting(cell, ApplicationConstant.REPORT_STRING_STATUS, ExcelReportStyleBuilder.subHeaderStyle);
		for (int i = 0; i <= reportingEnv.size(); i++) {
			cell = row.createCell(cellnum++);
			ExcelReportStyleBuilder.setValueWithFormatting(cell, ApplicationConstant.REPORT_STRING_INSTANCE, ExcelReportStyleBuilder.subHeaderStyle);
			cell = row.createCell(cellnum++);
			ExcelReportStyleBuilder.setValueWithFormatting(cell, ApplicationConstant.REPORT_STRING_EG, ExcelReportStyleBuilder.subHeaderStyle);
			cell = row.createCell(cellnum++);
			ExcelReportStyleBuilder.setValueWithFormatting(cell, ApplicationConstant.REPORT_STRING_VERSION, ExcelReportStyleBuilder.subHeaderStyle);
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
	private int createBody(int rownum, Sheet sheet, String referenceEnv, Map<String, Map<String, List<String[][]>>> reportData) {
		for (Entry<String, Map<String, List<String[][]>>> rowDataPerApplication : reportData.entrySet()) {
			String applicationName = rowDataPerApplication.getKey();
			Map<String, List<String[][]>> latestDeploymentsAllInstance = rowDataPerApplication.getValue();

			int rowsRequiredForApplication = 1;
			for (Map.Entry<String, List<String[][]>> rowDataPerApplicationPerInstance : latestDeploymentsAllInstance.entrySet())
				if (rowsRequiredForApplication < rowDataPerApplicationPerInstance.getValue().size())
					rowsRequiredForApplication = rowDataPerApplicationPerInstance.getValue().size();

			rownum = createRowsForApplication(applicationName, referenceEnv, rownum, sheet, latestDeploymentsAllInstance, rowsRequiredForApplication);
		}
		return rownum;
	}

	/**
	 * 
	 * @param applicationName
	 * @param referenceEnv
	 * @param currentRow
	 * @param sheet
	 * @param latestDeploymentsAllInstance
	 * @param rowsRequiredForApplication
	 * @return
	 */
	private int createRowsForApplication(String applicationName, String referenceEnv, int currentRow, Sheet sheet, Map<String, List<String[][]>> latestDeploymentsAllInstance, int rowsRequiredForApplication) {
		for (int rownum = 0; rownum < rowsRequiredForApplication; rownum++) {
			Row row = ExcelReportStyleBuilder.formatRow(sheet.createRow(currentRow), "body");
			int cellnum = 0;
			/* application name at column 1 */
			if (rownum == 0) {
				Cell cell = row.createCell(++cellnum);
				ExcelReportStyleBuilder.setValueWithFormatting(cell, applicationName, ExcelReportStyleBuilder.bodyStyle);
				/*
				 * application status at column 2 will be filled as per anomaly calculation
				 */
				cell = row.createCell(++cellnum);
				if (calculateAnomlay(referenceEnv, latestDeploymentsAllInstance)) {
					ExcelReportStyleBuilder.setValueWithFormatting(cell, "û", ExcelReportStyleBuilder.bodyStyleSpecialCharacterRed);
				} else {
					ExcelReportStyleBuilder.setValueWithFormatting(cell, "ü", ExcelReportStyleBuilder.bodyStyleSpecialCharacterGreen);
				}
			} else {
				Cell cell = row.createCell(++cellnum);
				ExcelReportStyleBuilder.setValueWithFormatting(cell, "", ExcelReportStyleBuilder.bodyStyle);
				cell = row.createCell(++cellnum);
				ExcelReportStyleBuilder.setValueWithFormatting(cell, "", ExcelReportStyleBuilder.bodyStyle);
			}

			for (Map.Entry<String, List<String[][]>> rowDataPerApplicationPerInstance : latestDeploymentsAllInstance.entrySet()) {
				cellnum = createRowForApplication(rownum, rowDataPerApplicationPerInstance, cellnum, row);
			}
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
	private int createRowForApplication(int rownum, Entry<String, List<String[][]>> rowDataPerApplicationPerInstance, int cellnum, Row row) {
		/* there are data for current row */
		Cell cell = null;
		if (rownum < rowDataPerApplicationPerInstance.getValue().size()) {
			String[][] param = rowDataPerApplicationPerInstance.getValue().get(rownum);
			cell = row.createCell(++cellnum);
			ExcelReportStyleBuilder.setValueWithFormatting(cell, param[0][0], param[0][1] == null ? ExcelReportStyleBuilder.bodyStyle : ExcelReportStyleBuilder.bodyStyleAlert);
			cell = row.createCell(++cellnum);
			ExcelReportStyleBuilder.setValueWithFormatting(cell, param[1][0], param[1][1] == null ? ExcelReportStyleBuilder.bodyStyle : ExcelReportStyleBuilder.bodyStyleAlert);
			cell = row.createCell(++cellnum);
			ExcelReportStyleBuilder.setValueWithFormatting(cell, param[2][0], param[2][1] == null ? ExcelReportStyleBuilder.bodyStyle : ExcelReportStyleBuilder.bodyStyleAlert);
		}
		/** there is no data for current row */
		else {
			cell = row.createCell(++cellnum);
			ExcelReportStyleBuilder.setValueWithFormatting(cell, "", ExcelReportStyleBuilder.bodyStyle);
			cell = row.createCell(++cellnum);
			ExcelReportStyleBuilder.setValueWithFormatting(cell, "", ExcelReportStyleBuilder.bodyStyle);
			cell = row.createCell(++cellnum);
			ExcelReportStyleBuilder.setValueWithFormatting(cell, "", ExcelReportStyleBuilder.bodyStyle);
		}
		return cellnum;
	}

	/**
	 * Calculates anomaly in the application state across different environments
	 * 
	 * @param referenceEnv
	 * @param latestDeploymentsAllInstance
	 * @return
	 */
	private boolean calculateAnomlay(String referenceEnv, Map<String, List<String[][]>> latestDeploymentsAllInstance) {
		latestDeploymentsAllInstance.get(referenceEnv).get(0)[0][1] = "true";
		return false;
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