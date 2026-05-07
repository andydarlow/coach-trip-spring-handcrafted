package org.darlow.coachtrip.controllers;

import org.darlow.coachtrip.models.CoachTripSchedule;
import org.darlow.coachtrip.services.CreateCoachTripScheduleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class CoachSchedulesRestValidationTest {


    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateCoachTripScheduleService mockScheduleService;

    @Test
    void returnsOkOnValidRequestTest() throws Exception {

        String requestBody = """
				{
				   "departureTime": "13:20",
				   "arrivalTime": "19:20",
				   "departureCity": "LONDON",
				   "arrivalCity": "MANCHESTER",
				   "scheduleDays": ["MONDAY","WEDNESDAY"]
				}
				""";
        when(mockScheduleService.createCoachTripSchedule(any())).thenReturn(
                CoachTripSchedule.builder().id(123L).build());
        mockMvc.perform(post("/coachSchedules").
                         content(requestBody).
                         contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"));
    }



    @Test
    void missingScheduledaysTest() throws Exception {

        String requestBody = """
				{
				   "departureTime": "13:20",
				   "arrivalTime": "19:20",
				   "departureCity": "LONDON",
				   "arrivalCity": "MANCHESTER",
				   "scheduleDays": []
				}
				""";
        when(mockScheduleService.createCoachTripSchedule(any())).thenReturn(
                CoachTripSchedule.builder().id(123L).build());
        mockMvc.perform(post("/coachSchedules").
                        content(requestBody).
                        contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("scheduleDays:size must be between 1 and 7"));
    }

    @Test
    void badScheduledaysNameTest() throws Exception {

        String requestBody = """
				{
				   "departureTime": "13:20",
				   "arrivalTime": "19:20",
				   "departureCity": "LONDON",
				   "arrivalCity": "MANCHESTER",
				   "scheduleDays": ["BADDAY"]
				}
				""";
        when(mockScheduleService.createCoachTripSchedule(any())).thenReturn(
                CoachTripSchedule.builder().id(123L).build());
        mockMvc.perform(post("/coachSchedules").
                        content(requestBody).
                        contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("scheduleDays:size must be between 1 and 7"));
    }

}