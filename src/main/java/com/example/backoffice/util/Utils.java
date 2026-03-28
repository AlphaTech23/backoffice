package com.example.backoffice.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.example.backoffice.model.Reservation;

public class Utils {
    public static boolean isBetween(LocalDateTime date, LocalDateTime d1, LocalDateTime d2) throws RuntimeException {
        if (d1.isAfter(d2))
            throw new RuntimeException("d1 must be <= d2");
        return (d1.isBefore(date) || d1.equals(date)) && (d2.isAfter(date) || d2.equals(date));
    }

    public static LocalTime getMaxHeureRetour(List<Reservation> list, LocalDateTime dateHeureDepart) {
        LocalTime heureDepart = list.stream()
                .map(Reservation::getDateArrivee)
                .filter(dt -> dt.isAfter(dateHeureDepart))
                .max(LocalDateTime::compareTo)
                .map(LocalDateTime::toLocalTime)
                .orElse(dateHeureDepart.toLocalTime());
        return heureDepart;
    }
}