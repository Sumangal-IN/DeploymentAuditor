package com.kingfisher.deployment.audit.report.builder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.kingfisher.deployment.audit.constant.ApplicationConstant;
import com.kingfisher.deployment.audit.data.model.Deployment;

@Component
public class ExcelReportBuilder {

	public byte[] createReport(String referenceEnv, List<String> reportingEnv, Map<String, Map<String, List<Deployment>>> reportData, String sheetName) throws IOException {
		Workbook workbook = new XSSFWorkbook();
		ExcelReportStyleBuilder.addStyles(workbook);
		Sheet sheet = workbook.createSheet(sheetName);

		int currentRow = 1;
		currentRow = createHeader(currentRow, sheet, referenceEnv, reportingEnv);
		currentRow = createSubHeader(currentRow, sheet, reportingEnv);
		createBody(currentRow, sheet, referenceEnv, reportData);

		for (int col = 0; col < 7 + (3 * reportingEnv.size()); col++)
			sheet.autoSizeColumn(col);

		return workbookToBye(workbook);
	}

	private void createBody(int currentRow, Sheet sheet, String referenceEnv, Map<String, Map<String, List<Deployment>>> reportData) {
		for (Map.Entry<String, Map<String, List<Deployment>>> rowDataPerApplication : reportData.entrySet()) {
			String application = rowDataPerApplication.getKey();
			Map<String, List<Deployment>> latestDeploymentsPerInstance = rowDataPerApplication.getValue();

			int rowsRequiredForApplication = 1;
			for (Map.Entry<String, List<Deployment>> rowDataPerApplicationPerInstance : latestDeploymentsPerInstance.entrySet())
				if (rowsRequiredForApplication < rowDataPerApplicationPerInstance.getValue().size())
					rowsRequiredForApplication = rowDataPerApplicationPerInstance.getValue().size();
			int applicationMainRow = 0;
			for (int i = 0; i < rowsRequiredForApplication; i++) {
				Row row = ExcelReportStyleBuilder.formatRow(sheet.createRow(currentRow), "body");
				if (i == 0)
					applicationMainRow = currentRow;
				boolean anomalyInApplication = false;
				int cellnum = 0;
				/* application name at column 1*/
				Cell cell = row.createCell(++cellnum);
				if (i == 0)
					ExcelReportStyleBuilder.setValueWithFormatting(cell, application, ExcelReportStyleBuilder.bodyStyle);
				else
					ExcelReportStyleBuilder.setValueWithFormatting(cell, "", ExcelReportStyleBuilder.bodyStyle);
				
				/* application status at column 2*/
				cell = row.createCell(++cellnum);
				ExcelReportStyleBuilder.setValueWithFormatting(cell, "", ExcelReportStyleBuilder.bodyStyle);

				for (Map.Entry<String, List<Deployment>> rowDataPerApplicationPerInstance : latestDeploymentsPerInstance.entrySet()) {
					if (i < rowDataPerApplicationPerInstance.getValue().size()) {
						Deployment deployment = rowDataPerApplicationPerInstance.getValue().get(i);
						cell = row.createCell(++cellnum);
						ExcelReportStyleBuilder.setValueWithFormatting(cell, deployment.getInstanceName(), ExcelReportStyleBuilder.bodyStyle);
						if (rowDataPerApplicationPerInstance.getKey().equals(referenceEnv)) {
							cell = row.createCell(++cellnum);
							ExcelReportStyleBuilder.setValueWithFormatting(cell, deployment.getIntegrationServer(), ExcelReportStyleBuilder.bodyStyle);
							cell = row.createCell(++cellnum);
							ExcelReportStyleBuilder.setValueWithFormatting(cell, deployment.getBarReleaseId(), ExcelReportStyleBuilder.bodyStyle);
						} else {
							cell = row.createCell(++cellnum);
							if (isEGMatch(deployment, latestDeploymentsPerInstance.get(referenceEnv))) {
								ExcelReportStyleBuilder.setValueWithFormatting(cell, deployment.getIntegrationServer(), ExcelReportStyleBuilder.bodyStyle);
							} else {
								anomalyInApplication = true;
								ExcelReportStyleBuilder.setValueWithFormatting(cell, deployment.getIntegrationServer(), ExcelReportStyleBuilder.bodyStyleAlert);
							}

							cell = row.createCell(++cellnum);
							if (isVersionMatch(deployment, latestDeploymentsPerInstance.get(referenceEnv))) {
								ExcelReportStyleBuilder.setValueWithFormatting(cell, deployment.getBarReleaseId(), ExcelReportStyleBuilder.bodyStyle);
							} else {
								anomalyInApplication = true;
								ExcelReportStyleBuilder.setValueWithFormatting(cell, deployment.getBarReleaseId(), ExcelReportStyleBuilder.bodyStyleAlert);
							}
						}
					} else {
						cell = row.createCell(++cellnum);
						ExcelReportStyleBuilder.setValueWithFormatting(cell, "", ExcelReportStyleBuilder.bodyStyle);
						cell = row.createCell(++cellnum);
						ExcelReportStyleBuilder.setValueWithFormatting(cell, "", ExcelReportStyleBuilder.bodyStyle);
						cell = row.createCell(++cellnum);
						ExcelReportStyleBuilder.setValueWithFormatting(cell, "", ExcelReportStyleBuilder.bodyStyle);
					}
				}

				/* update application status at column 2*/
				cell = sheet.getRow(applicationMainRow).getCell(2);
				if (anomalyInApplication)
					ExcelReportStyleBuilder.setValueWithFormatting(cell, "û", ExcelReportStyleBuilder.bodyStyleSpecialCharacterRed);
				else
					ExcelReportStyleBuilder.setValueWithFormatting(cell, "ü", ExcelReportStyleBuilder.bodyStyleSpecialCharacterGreen);

				currentRow++;
			}

		}
	}

	private boolean isVersionMatch(Deployment deployment, List<Deployment> referenceEnvDeployments) {
		for (Deployment referenceEnvDeployment : referenceEnvDeployments)
			if (referenceEnvDeployment.getBarReleaseId().equals(deployment.getBarReleaseId()))
				return true;
		return false;
	}

	private boolean isEGMatch(Deployment deployment, List<Deployment> referenceEnvDeployments) {
		for (Deployment referenceEnvDeployment : referenceEnvDeployments)
			if (referenceEnvDeployment.getIntegrationServer().equals(deployment.getIntegrationServer()))
				return true;
		return false;
	}

	private int createHeader(int currentRow, Sheet sheet, String referenceEnv, List<String> reportingEnv) {
		Row row = ExcelReportStyleBuilder.formatRow(sheet.createRow(currentRow), "header");
		int cellnum = 3;
		Cell cell = row.createCell(cellnum);
		ExcelReportStyleBuilder.setValueWithFormatting(cell, referenceEnv, ExcelReportStyleBuilder.headerStyle);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, cellnum, cellnum + 2));
		cellnum += 3;
		for (String env : reportingEnv) {
			cell = row.createCell(cellnum);
			ExcelReportStyleBuilder.setValueWithFormatting(cell, env, ExcelReportStyleBuilder.headerStyle);
			sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, cellnum, cellnum + 2));
			cellnum += 3;
		}
		return ++currentRow;
	}

	private int createSubHeader(int currentRow, Sheet sheet, List<String> reportingEnv) {
		Row row = ExcelReportStyleBuilder.formatRow(sheet.createRow(currentRow), "subHeader");
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
		return ++currentRow;
	}

	private byte[] workbookToBye(Workbook workbook) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		workbook.close();
		bos.close();
		return bos.toByteArray();
	}
}
