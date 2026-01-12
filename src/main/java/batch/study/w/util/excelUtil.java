package batch.study.w.util;

import batch.study.w.common.exception.BusinessException;
import batch.study.w.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class excelUtil {

	private static final long MAX_FILE_SIZE = 500 * 1024 * 1024L; // 500MB

	//Excel 파일 검증
	public static void validateExcelFile(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
		}

		String filename = file.getOriginalFilename();
		if (filename == null || (!filename.endsWith(".xlsx") && !filename.endsWith(".xls"))) {
			throw new BusinessException(ErrorCode.INVALID_FILE_TYPE);
		}

		// 파일 크기 체크
		if (file.getSize() > MAX_FILE_SIZE) {
			throw new BusinessException(ErrorCode.FILE_SIZE_EXCEEDED);
		}
	}

	//Excel 파일을 Workbook으로 변환
	public static Workbook createWorkbook(MultipartFile file) {
		try {
			String filename = file.getOriginalFilename();
			if (filename == null) {
				throw new BusinessException(ErrorCode.INVALID_FILE_TYPE);
			}

			if (filename.endsWith(".xlsx")) {
				return WorkbookFactory.create(file.getInputStream());
			} else {
				return new HSSFWorkbook(file.getInputStream());
			}
		} catch (IOException e) {
			log.error("Excel 파일 읽기 오류: ", e);
			throw new BusinessException(ErrorCode.EXCEL_READ_ERROR);
		}
	}

	//Excel 파일의 모든 시트를 읽어서 Map으로 반환
	public static Map<String, List<List<String>>> readAllSheets(MultipartFile file) {
		validateExcelFile(file);

		Map<String, List<List<String>>> result = new LinkedHashMap<>();

		try (Workbook workbook = createWorkbook(file)) {
			int numberOfSheets = workbook.getNumberOfSheets();

			for (int i = 0; i < numberOfSheets; i++) {
				Sheet sheet = workbook.getSheetAt(i);
				String sheetName = sheet.getSheetName();
				List<List<String>> sheetData = readSheet(sheet);

				result.put(sheetName, sheetData);
			}
		} catch (IOException e) {
			log.error("Excel 파일 처리 중 오류: ", e);
			throw new BusinessException(ErrorCode.EXCEL_READ_ERROR);
		} catch (Exception e) {
			log.error("Excel 파일 처리 중 예상치 못한 오류: ", e);
			throw new BusinessException(ErrorCode.EXCEL_READ_ERROR);
		}

		return result;
	}

	
	//특정 시트의 데이터를 읽어서 List로 반환
	public static List<List<String>> readSheet(Sheet sheet) {
		List<List<String>> sheetData = new ArrayList<>();

		int lastRowNum = sheet.getLastRowNum();
		for (int rowNum = 0; rowNum <= lastRowNum; rowNum++) {
			Row row = sheet.getRow(rowNum);
			if (row == null) {
				continue;
			}

			List<String> rowData = readRow(row);
			if (!rowData.isEmpty()) {
				sheetData.add(rowData);
			}
		}

		return sheetData;
	}

	
	//특정 행의 데이터를 읽어서 List로 반환
	public static List<String> readRow(Row row) {
		List<String> rowData = new ArrayList<>();
		int lastCellNum = row.getLastCellNum();

		for (int cellNum = 0; cellNum < lastCellNum; cellNum++) {
			Cell cell = row.getCell(cellNum);
			String cellValue = getCellValueAsString(cell);
			rowData.add(cellValue);
		}

		return rowData;
	}

	
	// Cell String로로 변환
	public static String getCellValueAsString(Cell cell) {
		if (cell == null) {
			return "";
		}

		switch (cell.getCellType()) {
			case STRING:
				return cell.getStringCellValue();
			case NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					return cell.getDateCellValue().toString();
				} else {
					// 정수인 경우 소수점 제거
					double numericValue = cell.getNumericCellValue();
					if (numericValue == (long) numericValue) {
						return String.valueOf((long) numericValue);
					} else {
						return String.valueOf(numericValue);
					}
				}
			case BOOLEAN:
				return String.valueOf(cell.getBooleanCellValue());
			case FORMULA:
				return cell.getCellFormula();
			case BLANK:
				return "";
			default:
				return "";
		}
	}

	/**
	 * 첫 번째 행이 헤더인지 확인
	 */
	public static boolean isHeaderRow(Row row) {
		if (row == null) {
			return false;
		}

		Cell firstCell = row.getCell(0);
		if (firstCell == null) {
			return false;
		}

		String firstCellValue = getCellValueAsString(firstCell).toLowerCase();
		return firstCellValue.contains("userid") || 
			   firstCellValue.contains("아이디") || 
			   firstCellValue.contains("id") ||
			   firstCellValue.contains("이름") ||
			   firstCellValue.contains("username") ||
			   firstCellValue.contains("사용자");
	}

	/**
	 * 헤더 행에서 컬럼 인덱스 찾기
	 */
	public static int findColumnIndex(Row headerRow, String... keywords) {
		if (headerRow == null) {
			return -1;
		}

		int lastCellNum = headerRow.getLastCellNum();
		for (int i = 0; i < lastCellNum; i++) {
			Cell cell = headerRow.getCell(i);
			if (cell == null) {
				continue;
			}

			String cellValue = getCellValueAsString(cell).toLowerCase().trim();
			for (String keyword : keywords) {
				if (cellValue.contains(keyword.toLowerCase())) {
					return i;
				}
			}
		}

		return -1;
	}
}

