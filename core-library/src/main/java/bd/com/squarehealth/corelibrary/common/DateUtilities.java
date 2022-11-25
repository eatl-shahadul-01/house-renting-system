package bd.com.squarehealth.corelibrary.common;

import java.util.Calendar;
import java.util.Date;

public final class DateUtilities {

    public static boolean isValidDateRange(Date from, Date to) {
        return from.getTime() < to.getTime();
    }

    public static boolean isValidFutureDate(Date date) {
        Date currentDate = new Date(System.currentTimeMillis());
        long currentDateInMilliseconds = currentDate.getTime();

        return date.getTime() > currentDateInMilliseconds;
    }

    // Note: could've made another class called DateRange to hold from and to dates.
    // but that would require another object to be created to call this method...
    public static boolean doDatesOverlap(Date fromA, Date toA, Date fromB, Date toB) {
        return toA.getTime() >= fromB.getTime() && fromA.getTime() <= toB.getTime();
    }

    public static Date subtractDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -days);

        return calendar.getTime();
    }
}
