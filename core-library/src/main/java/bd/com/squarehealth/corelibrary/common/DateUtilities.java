package bd.com.squarehealth.corelibrary.common;

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

    public static boolean doDatesOverlap(Date fromA, Date toA, Date fromB, Date toB) {
        return toA.getTime() >= fromB.getTime() && fromA.getTime() <= toB.getTime();
    }
}
