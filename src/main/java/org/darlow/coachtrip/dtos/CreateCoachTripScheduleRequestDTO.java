package org.darlow.coachtrip.dtos;

/*
  format of/validate incoming messages for coach schedules
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.darlow.coachtrip.models.CoachTripSchedule;
import org.darlow.coachtrip.models.DaysOfWeek;

import java.time.LocalTime;
import java.util.List;
@Data
public class CreateCoachTripScheduleRequestDTO {

    @NotNull(message = "Departure time is required")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime departureTime;
    @NotNull(message = "Arrival time is required")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime arrivalTime;

    @NotNull(message = "Departure city is required")
    @Size(min=3, max = 100, message = "city must be between 3 and 100 character")
    private String departureCity;

    @NotNull(message = "Arrival city is required")
    @Size(min=3, max = 100, message = "city must be between 3 and 100 character")
    private String arrivalCity;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    @Size(min = 1, max = 7) // 7 days in a week
    private List<DaysOfWeek> scheduleDays;

    public CoachTripSchedule toCoachTripSchedule() {
        return CoachTripSchedule.builder()
                .arrivalTime(arrivalTime)
                .DepartureTime(departureTime)
                .operatingDays(scheduleDays)
                .arrivalCity(arrivalCity)
                .departureCity(departureCity)
                .build();
    }
}
