package my.com.selenex.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.IteratorUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import my.com.selenex.vo.Cascade;
import my.com.selenex.vo.ResultReport;
import my.com.selenex.vo.Scenario;
import my.com.selenex.vo.Table;

/**
 * 
 * @author Fa'izam
 *
 */
public class DataSheet {
	
	protected Logger logger;
	protected HSSFWorkbook workbook;
	protected HSSFWorkbook outputWorkbook;
	protected String outputFileName;
	protected int eventRowNum;
	protected int resultRowNum;
	protected FileOutputStream outputStream;
	protected JSONObject actions;

	/**
	 * 
	 * @param excelFile
	 */
	public DataSheet(String excelFile) {
		try {
			this.workbook = new HSSFWorkbook(new FileInputStream(excelFile));
			outputFileName = excelFile.replace(".xls", "_result.xls");
			generateResultFile(outputFileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param input
	 * @param resultPath
	 * @param logger
	 */
	public DataSheet(File input, String resultPath, Logger logger) {
		this.logger = logger;
		try {
			this.workbook = new HSSFWorkbook(new FileInputStream(input));
			outputFileName = input.getName().replace(".xls", "_result.xls");
			generateResultFile(resultPath + File.separator + outputFileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param input
	 * @param resultPath
	 * @param logger
	 */
	public DataSheet(File input, String resultPath, Logger logger, JSONObject actions) {
		this.logger = logger;
		this.actions = actions;
		try {
			this.workbook = new HSSFWorkbook(new FileInputStream(input));
			outputFileName = input.getName().replace(".xls", "_result.xls");
			generateResultFile(resultPath + File.separator + outputFileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected static final String scenarioPrefixSheet = "Scenario-";
	protected static final String[] scenarioColumns = { "Action", "Selector Type", "Selector String", "Value", "Note" };
	protected static final String[] outputHeaders = { 
			"Scenario ID", 
			"Action", 
			"Selector", 
			"Scenario Description", 
			"Expected", 
			"Actual", 
			"Result", 
			"Time start", 
			"Time End",
	"Time Taken (ms)" };


	/**
	 * 
	 * @param cell
	 * @param color
	 */
	private void setFontColor(Cell cell, short color) {
		HSSFCellStyle style = outputWorkbook.createCellStyle();
		HSSFFont font = outputWorkbook.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setFontHeightInPoints((short) 10);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font.setColor(color);
		style.setFont(font);
		cell.setCellStyle(style);
	}

	/**
	 * 
	 * @param cell
	 * @param color
	 */
	private void setCellColor(Cell cell, short color) {
		HSSFCellStyle style = outputWorkbook.createCellStyle();
		HSSFFont font = outputWorkbook.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setFontHeightInPoints((short) 10);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		style.setFillPattern(CellStyle.ALIGN_FILL);
		style.setFillBackgroundColor(color);
		style.setFillForegroundColor(color);
		cell.setCellStyle(style);
	}

	/**
	 * Initiate output file
	 * 
	 * @param excelResult
	 * @throws IOException
	 */
	protected Map<String, Integer> resultRowCnts;
	private void generateResultFile(String excelResult) throws IOException {
		// Generate Output result
		if (resultRowCnts == null) {
			resultRowCnts = new HashMap<String, Integer>();
		}

		outputStream = new FileOutputStream(excelResult);
		outputWorkbook = new HSSFWorkbook();
		for (String sheetName: getScenarioSheets()) {
			resultRowCnts.put(sheetName, 1);
			HSSFSheet resultSheet = outputWorkbook.createSheet(sheetName);

			int i = 0;
			Row row = resultSheet.createRow(i);
			for (String header : outputHeaders) {
				Cell cell = row.createCell(i);
				cell.setCellValue(header);
				setCellColor(cell, HSSFColor.BLUE.index);
				i++;
			}
		}
	}

	public void writeOutputToExcel() throws IOException {
		//logger.info("Do writing to file");
		this.outputWorkbook.write(outputStream);
		outputStream.close();
	}

	/**
	 * 
	 * @param result
	 */
	public void addTestResult(ResultReport resultReport,  String sheetName) {
		if (resultRowCnts == null) {
			resultRowCnts = new HashMap<String, Integer>();
			resultRowCnts.put(sheetName, 1);
		}

		String[] str = new String[outputHeaders.length];
		str[0] = resultReport.getScenarioId();
		str[1] = resultReport.getAction();
		str[2] = resultReport.getSelectorType() + ":" + resultReport.getSelectorString();
		str[3] = resultReport.getDescription();
		str[4] = resultReport.getExpected();
		str[5] = resultReport.getActual();
		str[6] = resultReport.getResult();

		String startDt = new SimpleDateFormat("HH:mm:ss:SSS").format(resultReport.getStart());
		str[7] = startDt;

		String endDt = new SimpleDateFormat("HH:mm:ss:SSS").format(resultReport.getEnd());
		str[8] = endDt;
		str[9] = resultReport.getConsumingTime();

		HSSFSheet sheet = outputWorkbook.getSheet(sheetName);
		Row row = sheet.createRow(resultRowCnts.get(sheetName));
		for (int i = 0; i < outputHeaders.length; i++) {
			Cell cell = row.createCell(i);
			cell.setCellValue(str[i]);
			if (i == 6) {
				if (str[i].equals("FAIL")) {
					setFontColor(cell, HSSFColor.RED.index);
				}
				else {
					setFontColor(cell, HSSFColor.GREEN.index);
				}
			}
		}
		resultRowCnts.put(sheetName, resultRowCnts.get(sheetName)+ 1);
	}

	/**
	 * 
	 * @param row
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<Cell> readCellsInrow(Row row) {
		Iterator<Cell> cellIterator = row.cellIterator();
		return IteratorUtils.toList(cellIterator);
	}



	/**
	 * 
	 * @param row
	 * @return
	 */
	protected Scenario readCellsInrow(Row row, String sheetName) {
		Cell cell;
		Iterator<Cell> cellIterator = row.cellIterator();
		String cellValue;
		Scenario scenario = new Scenario();
		scenario.setScenarioTagged(sheetName);

		while (cellIterator.hasNext()) {
			cell = cellIterator.next();
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BOOLEAN:
				cellValue = String.valueOf(cell.getBooleanCellValue());
				break;

			case Cell.CELL_TYPE_NUMERIC:
				cellValue = String.valueOf(cell.getNumericCellValue());
				break;

			case Cell.CELL_TYPE_STRING:
				cellValue = String.valueOf(cell.getStringCellValue());
				break;

			default:
				cellValue = "";
			}

			switch (scenarioColumns[cell.getColumnIndex()]) {
			case "Action":
				scenario.setAction(cellValue);
				break;

			case "Selector Type":
				scenario.setSelectorType(cellValue);
				break;

			case "Selector String":
				scenario.setSelectorString(cellValue);
				break;

			case "Value":
				scenario.setValue(cellValue);
				break;

			case "Note":
				scenario.setNote(cellValue);
				break;

			default:
				break;
			}
		}

		return scenario;
	}

	/**
	 * 
	 * @param sheetName
	 * @return
	 * @throws Exception
	 */
	public List<String> getHeaderList(String sheetName) throws Exception {

		HSSFSheet sheet = workbook.getSheet(sheetName);
		Row row = null;
		Iterator<Row> rowIterator = sheet.iterator();

		List<String> headers = new ArrayList<String>();
		// Get the header index
		if (rowIterator.hasNext()) {
			row = rowIterator.next();
			headers.addAll(getRowValue(row));
		}

		return headers;
	}

	/**
	 * 
	 * @param sheetName
	 * @return
	 * @throws Exception
	 */
	private List<LinkedHashMap<String, String>> populateExcelInput(String sheetName) throws Exception {

		HSSFSheet sheet = workbook.getSheet(sheetName);
		Row row = null;
		Iterator<Row> rowIterator = sheet.iterator();

		List<String> headers = new ArrayList<String>();
		// Get the header index
		if (rowIterator.hasNext()) {
			row = rowIterator.next();
			headers.addAll(getRowValue(row));
		}

		// Put input entries into map
		List<LinkedHashMap<String, String>> results = new ArrayList<LinkedHashMap<String, String>>();
		while (rowIterator.hasNext()) {
			row = rowIterator.next();
			LinkedHashMap<String, String> rowsMap = new LinkedHashMap<String, String>();
			for (Cell cell : readCellsInrow(row)) {
				rowsMap.put(headers.get(cell.getColumnIndex()), cell.getStringCellValue());
			}
			results.add(rowsMap);
			//logger.info("rowsMap);
		}

		return results;
	}

	/**
	 * 
	 * @param sheetName
	 * @return
	 * @throws Exception
	 */
	private List<Cascade> populateCascadeValidationDetails(String sheetName) throws Exception {

		HSSFSheet sheet = workbook.getSheet(sheetName);
		Row row = null;
		Iterator<Row> rowIterator = sheet.iterator();

		List<String> headers = new ArrayList<String>();
		// Get the header index
		if (rowIterator.hasNext()) {
			row = rowIterator.next();
			headers.addAll(getRowValue(row));
		}

		//Put annotation details entries into map
		List<Cascade> result = new ArrayList<>();
		while (rowIterator.hasNext()) {
			row = rowIterator.next();
			Cascade cascade = new Cascade();
			for (Cell cell : readCellsInrow(row)) {
				String header = headers.get(cell.getColumnIndex());
				String cellString = cell.getStringCellValue();
				switch (header.toLowerCase()) {
				case Cascade.PARENT:
					cascade.setParent(cellString);
					break;

				case Cascade.CHILD:
					cascade.setChild(cellString);
					break;
					
				case Cascade.REGEX:
					cascade.setRegex(cellString);
					break;
				
				case Cascade.REMARK:
					cascade.setRemark(cellString);
					break;

				default:
					logger.info("Column " +  cell.getStringCellValue() + " is not supported for @cascade annotation");
					break;
				}
			}
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			logger.info("populateCascadeValidationDetails(" + sheetName + "):" + gson.toJson(cascade));
			if (cascade.validateMandatoryFields()) {
				logger.info("Adding "+ gson.toJson(cascade));
				result.add(cascade);
			}

		}
		return result;
	}
	
	/**
	 * 
	 * @param sheetName
	 * @return
	 * @throws Exception
	 */
	private List<Table> populateTablesValidationDetails(String sheetName) throws Exception {
		System.out.println("@@@@sheetName:"+ sheetName);
		logger.info("@@@@sheetName:"+ sheetName);
		HSSFSheet sheet = workbook.getSheet(sheetName);
		Row row = null;
		Iterator<Row> rowIterator = sheet.iterator();

		List<String> headers = new ArrayList<String>();
		// Get the header index
		if (rowIterator.hasNext()) {
			row = rowIterator.next();
			headers.addAll(getRowValue(row));
		}

		//Put annotation details entries into map
		//LinkedHashMap<Integer, String> results = new LinkedHashMap<Integer, String>();
		
		List<Table> results = new ArrayList<>();
		while (rowIterator.hasNext()) {
			row = rowIterator.next();
			Table table = new Table();
			for (Cell cell : readCellsInrow(row)) {
				String header = headers.get(cell.getColumnIndex());
				String cellString = cell.getStringCellValue();
				switch (header.toLowerCase()) {
				case Table.KEY:
					
					String annotation ="@column-";
					if (cellString.startsWith(annotation)) {
						int idx = header.indexOf(annotation);
						int colNo = Integer.parseInt(cellString.substring(idx + annotation.length() + 1));
						table.setKey(colNo);
					}	
					break;

				case Table.REGEX:
					table.setRegex(cellString);
					break;
					
				case Table.REMARK:
					table.setRemark(cellString);
					break;

				default:
					logger.info("Column " +  cell.getStringCellValue() + " is not supported for @column annotation");
					break;
				}
			}
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			logger.info("populateTablesValidationDetails(" + sheetName + "):" + gson.toJson(table));
			if (table.validateMandatoryFields()) {
				logger.info("Adding "+ gson.toJson(table));
				results.add(table);
			}

		}
		return results;
	}

	/**
	 * 
	 * @param sheetName
	 * @param row
	 * @return
	 * @throws Exception
	 */
	private List<String> getRowValue(Row row) throws Exception {
		List<String> result = new ArrayList<String>();
		for (Cell cell : readCellsInrow(row)) {
			result.add(cell.getStringCellValue());
		}
		return result;
	}

	/**
	 * 
	 * @param cell
	 * @return
	 */
	protected String getCellValue(Cell cell) {
		String cellValue = null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BOOLEAN:
			cellValue = String.valueOf(cell.getBooleanCellValue());
			break;

		case Cell.CELL_TYPE_NUMERIC:
			cellValue = String.valueOf(cell.getNumericCellValue());
			break;

		case Cell.CELL_TYPE_STRING:
			cellValue = String.valueOf(cell.getStringCellValue());
			break;

		default:
			cellValue = "";
		}
		return cellValue;
	}

	/**
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<String> getScenarioSheets() throws FileNotFoundException, IOException {
		List<String> scenarioTags = new ArrayList<String>();
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			HSSFSheet sheet = workbook.getSheetAt(i);
			if (sheet.getSheetName().startsWith(scenarioPrefixSheet)) {
				scenarioTags.add(sheet.getSheetName());
			}
		}
		return scenarioTags;
	}



	/**
	 * 
	 * @param inputFile
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<Scenario> loadScenarioByScenarioSheet(String scenarioSheet) throws FileNotFoundException, IOException {
		List<Scenario> scenarios = new ArrayList<Scenario>();
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			HSSFSheet sheet = workbook.getSheetAt(i);
			if (sheet.getSheetName().equals(scenarioSheet)) {
				Row row;
				Iterator<Row> rowIterator = sheet.iterator();
				while (rowIterator.hasNext()) {
					row = rowIterator.next();
					// Skip the header in row 0
					if (row.getRowNum() > 0) {
						scenarios.add(readCellsInrow(row, sheet.getSheetName()));
					}
				}
			}
		}


		return scenarios;
	}

	/**
	 * 
	 * @param inputFile
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<Scenario> loadScenario() throws FileNotFoundException, IOException {
		List<Scenario> scenarios = new ArrayList<Scenario>();
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			HSSFSheet sheet = workbook.getSheetAt(i);
			if (sheet.getSheetName().startsWith(scenarioPrefixSheet)) {
				Row row;
				Iterator<Row> rowIterator = sheet.iterator();
				while (rowIterator.hasNext()) {
					row = rowIterator.next();
					// Skip the header in row 0
					if (row.getRowNum() > 0) {
						scenarios.add(readCellsInrow(row, sheet.getSheetName()));
					}
				}
			}
		}

		// logger.info("scenarios:" + scenarios);
		return scenarios;
	}



	/**
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<String> getInputKeys(String scenarioSheet) throws FileNotFoundException, IOException {
		List<String> keys = new ArrayList<String>();
		for (Scenario scenario : loadScenario()) {
			if (!scenario.getScenarioTagged().equals(scenarioSheet)) {
				continue;
			}
			if (scenario.getAction().equals("input")) {
				if (scenario.getValue().charAt(0) != '&') {
					continue;
				}
				String key = scenario.getValue().substring(1);
				// Avoid duplicated input
				if (!keys.contains(key)) {
					keys.add(key);
				}
			}
		}
		return keys;
	}

	

	/**
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<String> getAnnotationKey(String scenarioSheet) throws FileNotFoundException, IOException {
		List<String> keys = new ArrayList<String>();
		for (Scenario scenario : loadScenario()) {
			if (!scenario.getScenarioTagged().equals(scenarioSheet)) {
				continue;
			}
			if (scenario.getAction().equals("validate-column")) {
				if (!scenario.getValue().startsWith("@table.")) {
					continue;
				}
				String key = scenario.getValue().substring(1);
				// Avoid duplicated input
				if (!keys.contains(key)) {
					keys.add(key);
				}
			}
		}
		return keys;
	}

	/**
	 * Get All input Sheet Names from keys extracted from scenario
	 * 
	 * @param keys
	 * @return
	 */
	public List<String> extractSheetsNameFromKeys(List<String> keys) {
		List<String> sheetNames = new ArrayList<String>();
		for (String key : keys) {
			int idx = key.indexOf(".");
			String sheetName = key.substring(0, idx);
			if (!sheetNames.contains(sheetName)) {
				sheetNames.add(sheetName);
			}
		}
		return sheetNames;

	}

	/**
	 * Load all sheet in base on what have been set scenario
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public LinkedHashMap<?,?> prepareInputByScenarioSheet(String scenarioSheet)
			throws FileNotFoundException, IOException {

		LinkedHashMap<String, List<LinkedHashMap<String, String>>> input = new LinkedHashMap<>();
		List<String> sheetNames = new ArrayList<String>();
		for (Scenario scenario : loadScenario()) {
			if (!scenario.validateMandatoryField()) {
				continue;
			}
			
			if (!scenarioSheet.equals(scenario.getScenarioTagged())) {
				continue;
			}
			if (scenario.getAction().equals("input")) {
				int idx = scenario.getValue().indexOf(".");
				String sheetName = scenario.getValue().substring(1, idx);

				// Avoid duplicated input
				if (!sheetNames.contains(sheetName)) {
					sheetNames.add(sheetName);
					try {
						input.put(sheetName, populateExcelInput(sheetName));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		logger.info("prepareInputByScenarioSheet(" + scenarioSheet + "):" + gson.toJson(input));
		return input;
	}

	
	/**
	 * 
	 * Validation column base on header
	 * syntax: column-<No of column>
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public LinkedHashMap<String, List<Cascade>> prepareCascadeValidation(String scenarioSheet)
			throws FileNotFoundException, IOException {

		LinkedHashMap<String,List<Cascade>> annotations = new LinkedHashMap<>();
		List<String> sheetNames = new ArrayList<String>();
		for (Scenario scenario : loadScenario()) {
			if (!scenario.validateMandatoryField()) {
				continue;
			}
			
			if (!scenarioSheet.equals(scenario.getScenarioTagged())) {
				continue;
			}
			if (scenario.getAction().equals("validate-cascade")) {
				String annotation = "@cascade.";
				int idx = scenario.getValue().indexOf(annotation);
				String sheetName = scenario.getValue().substring(idx + annotation.length());

				// Avoid duplicated input
				if (!sheetNames.contains(sheetName)) {
					sheetNames.add(sheetName);
					try {
						annotations.put(sheetName, populateCascadeValidationDetails(sheetName));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				logger.info("prepareCascadeValidation(" + scenarioSheet + "):" + gson.toJson(annotations));
			}
		}
		return annotations;
	}
	
	/**
	 * 
	 * Validation column base on header
	 * syntax: column-<No of column>
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public LinkedHashMap<String, ?> tableColumnValidationRegex(String scenarioSheet)
			throws FileNotFoundException, IOException {

		LinkedHashMap<String, List<Table>> annotations = new LinkedHashMap<>();
		List<String> sheetNames = new ArrayList<String>();
		for (Scenario scenario : loadScenario()) {
			if (!scenario.validateMandatoryField()) {
				continue;
			}
			
			if (!scenarioSheet.equals(scenario.getScenarioTagged())) {
				continue;
			}
			if (scenario.getAction().equals("validate-column")) {
				String annotation = "@table.";
				int idx = scenario.getValue().indexOf(annotation);
				String sheetName = scenario.getValue().substring(idx + annotation.length());

				// Avoid duplicated input
				if (!sheetNames.contains(sheetName)) {
					sheetNames.add(sheetName);
					try {
						annotations.put(sheetName, populateTablesValidationDetails(sheetName));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				logger.info("tableColumnValidationRegex(" + scenarioSheet + "):" + gson.toJson(annotations));
			}
		}
		return annotations;
	}

	/**
	 * Load all sheet in base on what have been set scenario
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public LinkedHashMap<String, List<LinkedHashMap<String, String>>> prepareInput()
			throws FileNotFoundException, IOException {

		LinkedHashMap<String, List<LinkedHashMap<String, String>>> input = new LinkedHashMap<>();
		List<String> sheetNames = new ArrayList<String>();
		for (Scenario scenario : loadScenario()) {
			if (scenario.getAction().equals("input")) {
				int idx = scenario.getValue().indexOf(".");
				String sheetName = scenario.getValue().substring(1, idx);

				// Avoid duplicated input
				if (!sheetNames.contains(sheetName)) {
					sheetNames.add(sheetName);
					try {
						input.put(sheetName, populateExcelInput(sheetName));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return input;
	}

	/**
	 * 
	 * @param mapSet
	 * @param result
	 * @param depth
	 * @param current
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void GeneratePermutations(LinkedHashMap mapSet, List<LinkedHashMap<?,?>> result, int depth,
			LinkedHashMap current) {

		if (depth == mapSet.keySet().size()) {
			result.add((LinkedHashMap) current.clone());
			return;
		}

		List element = (List) (new ArrayList<LinkedHashMap>(mapSet.values())).get(depth);
		for (int i = 0; i < element.size(); ++i) {
			current.putAll((Map) element.get(i));
			GeneratePermutations(mapSet, result, depth + 1, current);
		}

	}


	/**
	 * 
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public List<LinkedHashMap<?,?>> generateInputByScenarioSheet(String scenarioSheet) throws Exception {
		
		logger.info("generateInputByScenarioSheet().Sheet Name:" + scenarioSheet);
		List<LinkedHashMap<?,?>> result = new ArrayList<>();
		LinkedHashMap<?,?> inputs = prepareInputByScenarioSheet(scenarioSheet);

		LinkedHashMap<?,?> current = new LinkedHashMap<>();
		GeneratePermutations(inputs, result, 0, current);
		return result;

	}



	/**
	 * 
	 * @param strId
	 */
	public void addNewLine(String strId, String sheetName) {
		String newLine[] = {strId,"","","","","","","","", "",""};
		HSSFSheet sheet = outputWorkbook.getSheet(sheetName);
		Row row = sheet.createRow(resultRowCnts.get(sheetName));
		for (int i = 0; i < outputHeaders.length; i++) {
			//logger.info("Entries : " + i + " = " +  newLine[i]);
			Cell cell = row.createCell(i);
			cell.setCellValue(newLine[i]);
			setCellColor(cell, HSSFColor.LIGHT_TURQUOISE.index);
		}
		resultRowCnts.put(sheetName, resultRowCnts.get(sheetName)+ 1);
	}

}