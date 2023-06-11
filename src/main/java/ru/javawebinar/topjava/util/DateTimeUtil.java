package ru.javawebinar.topjava.util;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T lt, T start, T end) {
        return (start == null || lt.compareTo(start) >= 0) && (end == null || lt.compareTo(end) < 0);
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalDate getDateOrNullFromRequest(HttpServletRequest request, String param) {
        String dateFromRequest = request.getParameter(param);
        return dateFromRequest != null &&
                !dateFromRequest.isEmpty() ?
                LocalDate.parse(dateFromRequest) :
                null;
    }
    public static LocalTime getTimeOrNullFromRequest(HttpServletRequest request, String param) {
        String timeFromRequest = request.getParameter(param);
        return timeFromRequest != null &&
                !timeFromRequest.isEmpty() ?
                LocalTime.parse(timeFromRequest) :
                null;
    }
}

