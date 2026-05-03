package vn.dainv.btl.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class DateTextUtil {

    private static final String PATTERN = "yyyy-MM-dd";

    private DateTextUtil() {
    }

    public static boolean isValidYyyyMmDd(String s) {
        if (s == null) return false;
        String t = s.trim();
        if (t.isEmpty()) return false;
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN, Locale.US);
        sdf.setLenient(false);
        try {
            sdf.parse(t);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static String todayIso() {
        return new SimpleDateFormat(PATTERN, Locale.US).format(new Date());
    }
}
