package com.bioskuy.api.controller;

import com.bioskuy.api.model.Movie;
import com.bioskuy.api.model.Schedule;
import com.bioskuy.api.model.Theater;
import com.bioskuy.api.repository.MovieRepository;
import com.bioskuy.api.repository.ScheduleRepository;
import com.bioskuy.api.repository.TheaterRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ScheduleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    private Theater testTheater;

    @BeforeEach
    void setUp() {
        // Clean up the database before each test
        scheduleRepository.deleteAll();
        movieRepository.deleteAll();
        theaterRepository.deleteAll();

        // Create a test movie
        Movie testMovie = new Movie();
        testMovie.setTitle("Integration Test Movie");
        testMovie.setGenre("Action");
        testMovie.setDuration(120);
        testMovie.setRating(4.5);
        testMovie = movieRepository.save(testMovie);

        // Create a test theater
        testTheater = new Theater();
        testTheater.setName("Integration Test Theater");
        testTheater.setLocation("Test Location");
        testTheater = theaterRepository.save(testTheater);

        // Create test schedules
        Schedule testSchedule1 = new Schedule();
        testSchedule1.setMovie(testMovie);
        testSchedule1.setTheater(testTheater);
        testSchedule1.setShowingDate(LocalDate.now());
        testSchedule1.setShowingTime(LocalTime.of(14, 0));
        testSchedule1.setTicketPrice(50.0);
        scheduleRepository.save(testSchedule1);

        Schedule testSchedule2 = new Schedule();
        testSchedule2.setMovie(testMovie);
        testSchedule2.setTheater(testTheater);
        testSchedule2.setShowingDate(LocalDate.now().plusDays(1));
        testSchedule2.setShowingTime(LocalTime.of(16, 0));
        testSchedule2.setTicketPrice(60.0);
        scheduleRepository.save(testSchedule2);
    }

    @AfterEach
    void tearDown() {
        // Clean up the database after each test
        scheduleRepository.deleteAll();
        movieRepository.deleteAll();
        theaterRepository.deleteAll();
    }

    @Test
    void testGetAllSchedules() throws Exception {
        mockMvc.perform(get("/schedules")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Retrieved 2 Schedule(s) (Page 1 of 1)"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content", hasSize(2)))
                .andExpect(jsonPath("$.data.content[0].movie.title").value("Integration Test Movie"))
                .andExpect(jsonPath("$.data.content[0].showingDate").isNotEmpty())
                .andExpect(jsonPath("$.data.content[0].showingTime").isNotEmpty())
                .andExpect(jsonPath("$.data.content[0].ticketPrice").value(50.0))
                .andExpect(jsonPath("$.data.content[1].ticketPrice").value(60.0));
    }

    @Test
    void testGetAllSchedulesWithDescendingSort() throws Exception {
        mockMvc.perform(get("/schedules")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("direction", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Retrieved 2 Schedule(s) (Page 1 of 1)"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content", hasSize(2)))
                .andExpect(jsonPath("$.data.content[0].ticketPrice").value(60.0))
                .andExpect(jsonPath("$.data.content[1].ticketPrice").value(50.0));
    }

    @Test
    void testGetSchedulesByTheaterId() throws Exception {
        mockMvc.perform(get("/schedules/theater/" + testTheater.getId())
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Retrieved 2 Schedule(s) From Theater Integration Test Theater (Page 1 of 1)"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content", hasSize(2)))
                .andExpect(jsonPath("$.data.content[0].movie.title").value("Integration Test Movie"))
                .andExpect(jsonPath("$.data.content[0].ticketPrice").value(50.0))
                .andExpect(jsonPath("$.data.content[1].ticketPrice").value(60.0));
    }

    @Test
    void testGetSchedulesByNonExistingTheaterId() throws Exception {
        long nonExistentId = 999L;
        mockMvc.perform(get("/schedules/theater/" + nonExistentId)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("direction", "asc"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }
}
