package org.darlow.coachtrip.services;
/*
   business logic/validation for all coach trips scheduling
 */

import org.darlow.coachtrip.models.CoachTripSchedule;
import org.darlow.coachtrip.repostiories.CoachScheduleRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class CreateCoachTripScheduleService {

    private final CoachScheduleRespository repository;

    @Autowired
    public CreateCoachTripScheduleService(CoachScheduleRespository coachScheduleRespository) {
        this.repository = coachScheduleRespository;
    }

    public CoachTripSchedule  createCoachTripSchedule(CoachTripSchedule coachSchedule) {
        if (repository.findOne(Example.of(coachSchedule)).isPresent())
                throw new CoachTripScheduleExistsException(coachSchedule);
        return repository.save(coachSchedule);
    }

}
