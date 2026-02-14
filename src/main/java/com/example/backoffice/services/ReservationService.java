package com.example.backoffice.services;

import com.example.backoffice.models.Reservation;
import com.example.backoffice.repositories.ReservationRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReservationService {

    private ReservationRepository repository = new ReservationRepository();

    public List<Reservation> getByDateArrive(String dateStr) throws Exception {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return repository.getAll();
        }

        // Expected format yyyy-MM-dd
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime startOfDay = date.atStartOfDay();
        
        return repository.getByDateArrive(startOfDay);
    }
}
