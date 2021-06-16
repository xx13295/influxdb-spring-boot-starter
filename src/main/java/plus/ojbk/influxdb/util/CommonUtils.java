package plus.ojbk.influxdb.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wxm
 * @version 1.0
 * @since 2021/6/16 19:16
 */
public class CommonUtils {

    /**
     * 时间转换 Instant字符串时间转 LocalDateTime
     *
     * @param time
     * @return
     */
    public static LocalDateTime parseStringToLocalDateTime(String time) {
        DateTimeFormatter df = DateTimeFormatter.ISO_DATE_TIME;
        return LocalDateTime.parse(time, df);
    }

    /**
     * localDateTime 转 Instant
     * @param localDateTime
     * @return
     */
    public static Instant parseLocalDateTimeToInstant(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    private static final Pattern linePattern = Pattern.compile("_(\\w)");
    private static final Pattern humpPattern = Pattern.compile("[A-Z]");

    /**
     * 下划线转驼峰
     *
     * @param str
     * @return
     */
    public static String lineToHump(String str) {
        Matcher matcher = linePattern.matcher(str.toLowerCase());
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 驼峰转下划线,
     *
     * @param str
     * @return
     */
    public static String humpToLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
