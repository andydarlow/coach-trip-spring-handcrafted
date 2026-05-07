package org.darlow.coachtrip.controllers;
/*
  REST API for coach schedules.
 */

import org.darlow.coachtrip.dtos.CreateCoachTripScheduleRequestDTO;
import org.darlow.coachtrip.dtos.CreateCoachTripScheduleResponseDTO;
import org.darlow.coachtrip.models.CoachTripSchedule;
import org.darlow.coachtrip.services.CreateCoachTripScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coachSchedules")
public class CoachSchedules {

    private final CreateCoachTripScheduleService coachScheduleService;

    @Autowired
    public CoachSchedules(CreateCoachTripScheduleService coachScheduleService) {
        this.coachScheduleService = coachScheduleService;
    }

    /**
     * create a new coach schedule. This describes where a coach (Bus) travels to and when
     * @param newSchedule description on where a coach travels and when.
     * @return a summary containing the ID of the coach trip or an error if there was one.
     */
    @PostMapping
    public ResponseEntity<CreateCoachTripScheduleResponseDTO> creatCoachSchedule(@RequestBody @Validated CreateCoachTripScheduleRequestDTO newSchedule) {
        CoachTripSchedule savedSchedule = coachScheduleService.createCoachTripSchedule(newSchedule.toCoachTripSchedule());
        return ResponseEntity.ok().body(CreateCoachTripScheduleResponseDTO.toDTP(savedSchedule));
    }

}
