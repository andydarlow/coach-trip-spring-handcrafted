package org.darlow.coachtrip.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.darlow.coachtrip.models.CoachTripSchedule;

@Data
@Builder
public class CreateCoachTripScheduleResponseDTO {

    private Long id;

    public static CreateCoachTripScheduleResponseDTO toDTP(CoachTripSchedule newSchedule) {
        return builder()
                .id(newSchedule.getId())
                .build();


    }
}
