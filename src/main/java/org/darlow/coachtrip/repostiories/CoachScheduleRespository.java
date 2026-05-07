package org.darlow.coachtrip.repostiories;
/**
 * provides access to data on coach trip schedules (when and whereto a coach trip will travel)
 */

import org.darlow.coachtrip.models.CoachTripSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoachScheduleRespository extends JpaRepository<CoachTripSchedule, Long> {
}
