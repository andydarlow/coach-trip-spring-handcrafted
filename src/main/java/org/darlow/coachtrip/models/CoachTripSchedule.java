package org.darlow.coachtrip.models;

/**
 * coach trip schedule define when a coach (bus will travel from city to city. A traveller can book a seat
 * on a trip scheduled under this.
 */

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "coach_trip_schedule")
public class CoachTripSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection(targetClass = DaysOfWeek.class)
    @CollectionTable(
            name = "scheduled_days", // The name of the join table
            joinColumns = @JoinColumn(name = "trip_schedule_id") // Foreign key column
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_day")
    private List<DaysOfWeek> operatingDays;

    @Column(nullable = false)
    private LocalTime DepartureTime;

    @Column(nullable = false)
    private LocalTime arrivalTime;

    @Column(nullable = false)
    private String departureCity;

    @Column(nullable = false)
    private String arrivalCity;
}
