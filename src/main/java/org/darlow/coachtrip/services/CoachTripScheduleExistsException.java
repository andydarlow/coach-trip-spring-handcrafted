package org.darlow.coachtrip.services;
/**
 * trying to create a schedule that already exists
 */

import org.darlow.coachtrip.models.CoachTripSchedule;

public class CoachTripScheduleExistsException extends RuntimeException {
    public CoachTripScheduleExistsException(CoachTripSchedule coachSchedule)
    {
        super("Coach trip schedule already exists: " + coachSchedule);
    }
}
