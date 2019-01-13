package com.kingfisher.deployment.audit.report.builder;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

import com.kingfisher.deployment.audit.constant.ApplicationConstant;

public class ExcelReportStyleBuilder {
	public static CellStyle headerStyle;
	public static CellStyle subHeaderStyle;
	public static CellStyle bodyStyle;
	public static CellStyle bodyStyleSpecialCharacterGreen;
	public static CellStyle bodyStyleSpecialCharacterRed;
	public static CellStyle bodyStyleAlert;

	private ExcelReportStyleBuilder() {

	}

	public static void addStyles(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 11);
		font.setFontName(ApplicationConstant.FONT_CALIBRI);
		font.setColor(IndexedColors.WHITE.getIndex());

		subHeaderStyle = setBorders(workbook.createCellStyle());
		subHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
		subHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		subHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		subHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		subHeaderStyle.setFont(font);

		font = workbook.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setFontName(ApplicationConstant.FONT_CALIBRI);
		font.setBold(true);
		font.setColor(IndexedColors.WHITE.getIndex());

		headerStyle = setBorders(workbook.createCellStyle());
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		headerStyle.setFillForegroundColor(IndexedColors.BLUE1.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerStyle.setFont(font);

		font = workbook.createFont();
		font.setFontHeightInPoints((short) 11);
		font.setFontName(ApplicationConstant.FONT_CALIBRI);

		bodyStyle = setBorders(workbook.createCellStyle());
		bodyStyle.setAlignment(HorizontalAlignment.CENTER);
		bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		bodyStyle.setFont(font);

		font = workbook.createFont();
		font.setFontHeightInPoints((short) 14);
		font.setFontName(ApplicationConstant.FONT_WINGDINGS);
		font.setColor(IndexedColors.GREEN.getIndex());

		bodyStyleSpecialCharacterGreen = setBorders(workbook.createCellStyle());
		bodyStyleSpecialCharacterGreen.setAlignment(HorizontalAlignment.CENTER);
		bodyStyleSpecialCharacterGreen.setVerticalAlignment(VerticalAlignment.CENTER);
		bodyStyleSpecialCharacterGreen.setFont(font);

		font = workbook.createFont();
		font.setFontHeightInPoints((short) 14);
		font.setFontName(ApplicationConstant.FONT_WINGDINGS);
		font.setColor(IndexedColors.RED.getIndex());

		bodyStyleSpecialCharacterRed = setBorders(workbook.createCellStyle());
		bodyStyleSpecialCharacterRed.setAlignment(HorizontalAlignment.CENTER);
		bodyStyleSpecialCharacterRed.setVerticalAlignment(VerticalAlignment.CENTER);
		bodyStyleSpecialCharacterRed.setFont(font);

		font = workbook.createFont();
		font.setFontHeightInPoints((short) 11);
		font.setFontName(ApplicationConstant.FONT_CALIBRI);
		font.setColor(IndexedColors.RED.getIndex());

		bodyStyleAlert = setBorders(workbook.createCellStyle());
		bodyStyleAlert.setAlignment(HorizontalAlignment.CENTER);
		bodyStyleAlert.setVerticalAlignment(VerticalAlignment.CENTER);
		bodyStyleAlert.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
		bodyStyleAlert.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		bodyStyleAlert.setFont(font);
	}

	private static CellStyle setBorders(CellStyle style) {
		style.setTopBorderColor(IndexedColors.GREY_80_PERCENT.getIndex());
		style.setBorderTop(BorderStyle.THIN);
		style.setBottomBorderColor(IndexedColors.GREY_80_PERCENT.getIndex());
		style.setBorderBottom(BorderStyle.THIN);
		style.setLeftBorderColor(IndexedColors.GREY_80_PERCENT.getIndex());
		style.setBorderLeft(BorderStyle.THIN);
		style.setRightBorderColor(IndexedColors.GREY_80_PERCENT.getIndex());
		style.setBorderRight(BorderStyle.THIN);
		return style;
	}

	public static Row formatRow(Row row, String type) {
		switch (type) {
		case "body":
			row.setHeightInPoints(20f);
			break;
		case "header":
			row.setHeightInPoints(24f);
			break;
		case "subHeader":
			row.setHeightInPoints(24f);
			break;
		case "divider":
			row.setHeightInPoints(8f);
		default:
		}

		return row;
	}

	public static void setValueWithFormatting(Cell cell, String value, CellStyle style) {
		cell.setCellValue(value);
		cell.setCellStyle(style);
	}
}
