package org.darlow.coachtrip.controllers;

import org.darlow.coachtrip.models.CoachTripSchedule;
import org.darlow.coachtrip.services.CreateCoachTripScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

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


	private ResultActions callNewScheduleAPI(String jsonTestDate) throws Exception {
		return mockMvc.perform(post("/coachSchedules").
				content(jsonTestDate).
				contentType(MediaType.APPLICATION_JSON));
	}

	@BeforeEach
	public void setup() {
		when(mockScheduleService.createCoachTripSchedule(any())).thenReturn(
				CoachTripSchedule.builder().id(123L).build());
	}

	@Test
	void badJsonTest() throws Exception {

		callNewScheduleAPI("""
				{
				   "departureTime": "13:20",&&&
				   "arrivalTime": "19:20",
				   "departureCity": "LONDON",
				   "arrivalCity": "MANCHESTER",
				   "scheduleDays": ["MONDAY","WEDNESDAY"]
				}
				""")
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Invalid Message format: Unexpected character ('&' (code 38)): was expecting double-quote to start property name"))
				.andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.toString()));

		callNewScheduleAPI("""
				{
				   "departureTime": "13:20",
				   "arrivalTime": "19:20"
				   "departureCity": "LONDON",
				   "arrivalCity": "MANCHESTER",
				   "scheduleDays": ["MONDAY","WEDNESDAY"]
				}
				""")
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Invalid Message format: Unexpected character ('\"' (code 34)): was expecting comma to separate Object entries"))
				.andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.toString()));


		callNewScheduleAPI("""
				{
				   "departureTime": "13:20",
				   "arrivalTime": "19:20",
				   "departureCity": "LONDON",
				   "arrivalCity": "MANCHESTER",
				   "scheduleDays": ["MONDAY","WEDNESDAY]
				}
				""")
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Invalid Message format: Illegal unquoted character ((CTRL-CHAR, code 10)): has to be escaped using backslash to be included in string value"))
				.andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.toString()));

		callNewScheduleAPI("""
				{
				   "departureTime": "13:20",
				   "arrivalTime": "19:20",
				   "departureCity": "LONDON",
				   "arrivalCity": "MANCHESTER",
				   "scheduleDays": ["MONDAY","WEDNESDAY"]
				""")
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Invalid Message format: JSON parse error: Unexpected end-of-input: expected close marker"))
				.andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.toString()));
	}

	@Test
	void badjosn_emptyBodyTest() throws Exception {
		callNewScheduleAPI("""
				""")
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Invalid Message format: message not readable"))
				.andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.toString()));

		callNewScheduleAPI("""
				{}
				""")
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("arrivalCity:Arrival city is required,arrivalTime:Arrival time is required,departureCity:Departure city is required,departureTime:Departure time is required"))
				.andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.toString()));


	}



	@Test
	void badjosn_missingbracketTest() throws Exception {
		callNewScheduleAPI("""
				{
				   "departureTime": "13:20",
				   "arrivalTime": "19:20",
				   "departureCity": "LONDON",
				   "arrivalCity": "MANCHESTER",
				   "scheduleDays": ["MONDAY","WEDNESDAY"]
				""")
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Invalid Message format: JSON parse error: Unexpected end-of-input: expected close marker"))
				.andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.toString()));
	}

    @Test
    void returnsOkOnValidRequestTest() throws Exception {

		callNewScheduleAPI("""
				{
				   "departureTime": "13:20",
				   "arrivalTime": "19:20",
				   "departureCity": "LONDON",
				   "arrivalCity": "MANCHESTER",
				   "scheduleDays": ["MONDAY","WEDNESDAY"]
				}
				""")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"));
    }



    @Test
    void missingScheduledaysTest() throws Exception {

		callNewScheduleAPI( """
				{
				   "departureTime": "13:20",
				   "arrivalTime": "19:20",
				   "departureCity": "LONDON",
				   "arrivalCity": "MANCHESTER",
				   "scheduleDays": []
				}
				""")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("scheduleDays:size must be between 1 and 7"))
                .andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.toString()));

	}

    @Test
    void badScheduledaysNameTest() throws Exception {

		callNewScheduleAPI("""
				{
				   "departureTime": "13:20",
				   "arrivalTime": "19:20",
				   "departureCity": "LONDON",
				   "arrivalCity": "MANCHESTER",
				   "scheduleDays": ["BADDAY"]
				}
				""")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Field 'scheduleDays' cannot accept value 'BADDAY'"))
                .andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.toString()));
	}


	@Test
	void badDepartureTimeTest() throws Exception {

		callNewScheduleAPI("""
				{
				   "departureTime": "13:20.23",
				   "arrivalTime": "19:20",
				   "departureCity": "LONDON",
				   "arrivalCity": "MANCHESTER",
				   "scheduleDays": ["BADDAY"]
				}
				""")
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Invalid date/time format: 13:20.23. expected hh:mm"))
				.andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.toString()));
	}

	@Test
	void missingDepartureTimeTest() throws Exception {

		callNewScheduleAPI("""
				{
				   "arrivalTime": "19:20",
				   "departureCity": "LONDON",
				   "arrivalCity": "MANCHESTER",
				   "scheduleDays": ["MONDAY","THURSDAY"]
				}
				""")
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("departureTime:Departure time is required"))
				.andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.toString()));
	}

	@Test
	void blankDepartureTimeTest() throws Exception {

		callNewScheduleAPI("""
				{
				   "departureTime": "",
				   "arrivalTime": "19:20",
				   "departureCity": "LONDON",
				   "arrivalCity": "MANCHESTER",
				   "scheduleDays": ["WEDNESDAY"]
				}
				""")
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("departureTime:Departure time is required"))
				.andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.toString()));
	}


	@Test
	void missingArrivalTimeTest() throws Exception {

		callNewScheduleAPI("""
				{
				   "departureTime": "19:20",
				   "departureCity": "LONDON",
				   "arrivalCity": "MANCHESTER",
				   "scheduleDays": ["MONDAY","THURSDAY"]
				}
				""")
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("arrivalTime:Arrival time is required"))
				.andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.toString()));
	}

	@Test
	void blankArrivalTimeTest() throws Exception {

		callNewScheduleAPI("""
				{
				   "departureTime": "19:20",
				   "departureCity": "LONDON",
				   "arrivalCity": "MANCHESTER",
				   "scheduleDays": ["WEDNESDAY"]
				}
				""")
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("arrivalTime:Arrival time is required"))
				.andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.toString()));
	}

	@Test
	void blankDepartureCityTest() throws Exception {

		callNewScheduleAPI("""
				{
					"arrivalTime": "19:20",
				   "departureTime": "19:20",
				   "departureCity": "",
				   "arrivalCity": "MANCHESTER",
				   "scheduleDays": ["WEDNESDAY"]
				}
				""")
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("departureCity:city must be between 3 and 100 character"))
				.andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.toString()));
	}


	@Test
	void missingDepartureCityTest() throws Exception {

		callNewScheduleAPI("""
				{
					"arrivalTime": "19:20",
				   "departureTime": "19:20",
				   "arrivalCity": "MANCHESTER",
				   "scheduleDays": ["WEDNESDAY"]
				}
				""")
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("departureCity:Departure city is required"))
				.andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.toString()));
	}




	@Test
	void blankArrivalCityTest() throws Exception {

		callNewScheduleAPI("""
				{
					"arrivalTime": "19:20",
				   "departureTime": "19:20",
				   "departureCity": "LONDON",
				   "arrivalCity": "",
				   "scheduleDays": ["SATURDAY","SUNDAY"]
				}
				""")
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("arrivalCity:city must be between 3 and 100 character"))
				.andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.toString()));
	}


	@Test
	void missingArrivalCityTest() throws Exception {

		callNewScheduleAPI("""
				{
					"arrivalTime": "19:20",
				   "departureTime": "19:20",
				   "departureCity": "MANCHESTER",
				   "scheduleDays": ["FRIDAY"]
				}
				""")
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("arrivalCity:Arrival city is required"))
				.andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.toString()));
	}




	@Test
	void badArrivalTimeTest() throws Exception {

		callNewScheduleAPI("""
				{
				   "departureTime": "13:20",
				   "arrivalTime": "19:20.909",
				   "departureCity": "LONDON",
				   "arrivalCity": "MANCHESTER",
				   "scheduleDays": ["BADDAY"]
				}
				""")
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Invalid date/time format: 19:20.909. expected hh:mm"))
				.andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.toString()));
	}


}