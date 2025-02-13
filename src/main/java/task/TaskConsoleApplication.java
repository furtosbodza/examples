package task;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskConsoleApplication {

	private static final Logger LOGGER = Logger.getLogger(TaskConsoleApplication.class.getName());

	//private static final String FILE_DIR = "c:/Vica/work/sbp/Dualis_kepzes/szakmai_vizsgahoz/vica_work/idea/";

	public static void main(String[] args) {
		try {
			validateArgs(args);
			processFileImport(args);
		} catch (Exception ex) {
			LOGGER.log(Level.SEVERE, ex.getMessage());
		}
	}

	private static void processFileImport(String[] args) throws IOException, CsvValidationException {
		CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
		int recordCount = 0;
		try (CSVReader csvReader = new CSVReaderBuilder(
			new FileReader(getFileFullPath(args[0]), StandardCharsets.UTF_8)).withCSVParser(parser).build();) {

			String[] lineInArray;
			NumberModel dataModel = new NumberModel();
			while ((lineInArray = csvReader.readNext()) != null) {
				if (saveLine(lineInArray, dataModel)) {
					recordCount++;
				}
			}
		}
		LOGGER.log(Level.INFO, "A sikeresen importált rekordok száma: " + recordCount);
	}

	private static boolean saveLine(String[] line, NumberModel dataModel) {
		boolean ok = validateLine(line);
		if (!ok) {
			return false;
		}
		return saveToDb(line, dataModel);
	}

	private static boolean saveToDb(String[] line, NumberModel dataModel) {
		Number data = new Number();
		data.setSz1(Integer.valueOf(line[0]));
		data.setSz2(Integer.valueOf(line[1]));
		data.setSz3(Integer.valueOf(line[2]));
		data.setSz4(Integer.valueOf(line[3]));
		data.setSz5(Integer.valueOf(line[4]));
		data.setSz6(Integer.valueOf(line[5]));
		dataModel.saveNyeroszamok(data);
		return true;
	}

	private static boolean validateLine(String[] line) {
		for (String s : line) {
			if (StringUtils.isBlank(s)) {
				return false;
			}
			try {
				Integer.valueOf(s);
			} catch (NumberFormatException ne) {
				return false;
			}
		}
		return true;
	}

	private static void validateArgs(String[] args) {
		if (args == null || args.length != 1 || StringUtils.isBlank(args[0])) {
			throw new IllegalArgumentException("A paraméter listából hiányzik az importálandó cvs file neve! Pl.: data.csv");
		}
		validateFilePath(args[0]);
	}

	private static void validateFilePath(String fileName) {
		File file = new File(getFileFullPath(fileName));
		if (!file.exists()) {
			throw new IllegalArgumentException("Nem létezo fájl: " + file.getAbsolutePath());
		}
	}

	private static String getFileFullPath(String fileName) {
		String currentDirPath = new File("").getAbsolutePath();
		return currentDirPath + "/" + fileName;
	}

}
