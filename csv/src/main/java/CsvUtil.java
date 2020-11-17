import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class CsvUtil {

    public static void write(String content, String sheetName, String fileDirPath) {
        try {
            File dir = new File(fileDirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(getFile(fileDirPath, sheetName));
//            boolean alreadyExists = true;
            if (!file.exists()) {
                try {
//                    alreadyExists = false;
                    file.createNewFile();
                } catch (IOException e) {
//                    log.error(ExceptionUtil.getExceptionStack(e));
                }
            }
            CsvWriter csvOutput = new CsvWriter(new FileWriter(file, true), ',');
//            if(!alreadyExists) {
//                //write head
//                csvOutput.endRecord();
//            }
            csvOutput.write(content);
            csvOutput.write(String.valueOf(System.currentTimeMillis()));
            csvOutput.endRecord();
            csvOutput.close();
        } catch (Exception e) {
//            log.error(ExceptionUtil.getExceptionStack(e));
        }
    }

    public static Map<String, String> read(String sheetName, String fileDirPath) {
        Map<String, String> map = new LinkedHashMap<>();
        try {
            File file = new File(getFile(fileDirPath, sheetName));
            if (!file.exists()) {
                //do something with file not exist
            }
            FileInputStream input = new FileInputStream(file);
            CsvReader csvReader = new CsvReader(input, Charset.forName("UTF-8"));
            csvReader.setSafetySwitch(false);
            while (csvReader.readRecord()) {
                // use String value = csvReader.get(index) to get value in csv
            }
            csvReader.close();
        } catch (Exception e) {
//            log.error(ExceptionUtil.getExceptionStack(e));
        }
        return map;
    }

    private static String getFile(String fileDirPath, String fileName) {
        StringBuilder builder = new StringBuilder();
        if (Objects.nonNull(fileDirPath) && !fileDirPath.isEmpty()) {
            builder.append(fileDirPath);
        }
        if (Objects.nonNull(fileName) && !fileName.isEmpty()) {
            builder.append(fileName);
        }
        return builder.append(".csv").toString();
    }

}
