import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class DateUtils {

    /**
     * 缓存常用的DateTimeFormatter,提高性能,节约内存
     */
    private static Map<String, DateTimeFormatter> DATETIME_FORMATTER_MAP = new HashMap<>();

    private static DateTimeFormatter getDateTimeFormatter(String pattern) {
        DateTimeFormatter dateTimeFormatter = DATETIME_FORMATTER_MAP.get(pattern);
        if (dateTimeFormatter == null) {
            dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
            DATETIME_FORMATTER_MAP.put(pattern, dateTimeFormatter);
        }
        return dateTimeFormatter;
    }

    public static String formatTime(LocalDateTime localDateTime, String pattern) {
        if (localDateTime == null) {
            return null;
        }
        DateTimeFormatter dateTimeFormatter = getDateTimeFormatter(pattern);
        return dateTimeFormatter.format(localDateTime);
    }

    public static String formatDate(LocalDate localDate, String pattern) {
        if (localDate == null) {
            return null;
        }
        DateTimeFormatter dateTimeFormatter = getDateTimeFormatter(pattern);
        return dateTimeFormatter.format(localDate);
    }

    public static LocalDate parseLocalDate(String localdate, String pattern) {
        try {
            return LocalDate.parse(localdate, getDateTimeFormatter(pattern));
        } catch (Exception e) {
            return null;
        }
    }
}
