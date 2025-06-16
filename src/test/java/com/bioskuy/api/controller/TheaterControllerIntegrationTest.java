package com.bioskuy.api.controller;

import com.bioskuy.api.model.Movie;
import com.bioskuy.api.model.Schedule;
import com.bioskuy.api.model.Theater;
import com.bioskuy.api.repository.MovieRepository;
import com.bioskuy.api.repository.ScheduleRepository;
import com.bioskuy.api.repository.TheaterRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TheaterControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Theater testTheater1;
    private Theater testTheater2;
    private Movie testMovie;

    @BeforeEach
    void setUp() {
        // Configure ObjectMapper
        objectMapper.registerModule(new JavaTimeModule())
            .disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .configure(com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);

        // Clean up the database before each test
        scheduleRepository.deleteAll();
        theaterRepository.deleteAll();
        movieRepository.deleteAll();

        // Create test theaters
        testTheater1 = new Theater();
        testTheater1.setName("Integration Test Theater 1");
        testTheater1.setLocation("Integration Test Location 1");
        testTheater1.setSchedules(new ArrayList<>()); // Initialize with an empty list to avoid NPE

        testTheater2 = new Theater();
        testTheater2.setName("Integration Test Theater 2");
        testTheater2.setLocation("Integration Test Location 2");
        testTheater2.setSchedules(new ArrayList<>()); // Initialize with an empty list to avoid NPE

        // Save the theaters to the database
        testTheater1 = theaterRepository.save(testTheater1);
        testTheater2 = theaterRepository.save(testTheater2);

        // Create a test movie
        testMovie = new Movie();
        testMovie.setTitle("Integration Test Movie");
        testMovie.setGenre("Action");
        testMovie.setDuration(120);
        testMovie.setRating(4.5);

        // Save the movie to the database
        testMovie = movieRepository.save(testMovie);

        // Create test schedules
        Schedule testSchedule1 = new Schedule();
        testSchedule1.setMovie(testMovie);
        testSchedule1.setTheater(testTheater1);
        testSchedule1.setShowingDate(LocalDate.now());
        testSchedule1.setShowingTime(LocalTime.of(10, 0));
        testSchedule1.setTicketPrice(50.0);

        Schedule testSchedule2 = new Schedule();
        testSchedule2.setMovie(testMovie);
        testSchedule2.setTheater(testTheater2);
        testSchedule2.setShowingDate(LocalDate.now());
        testSchedule2.setShowingTime(LocalTime.of(13, 0));
        testSchedule2.setTicketPrice(60.0);

        // Save the schedules to the database
        scheduleRepository.save(testSchedule1);
        scheduleRepository.save(testSchedule2);
    }

    @AfterEach
    void tearDown() {
        // Clean up the database after each test
        scheduleRepository.deleteAll();
        theaterRepository.deleteAll();
        movieRepository.deleteAll();
    }

    @Test
    void testGetAllTheaters() throws Exception {
        // Set schedules to null to avoid circular references
        testTheater1.setSchedules(null);
        testTheater2.setSchedules(null);
        theaterRepository.save(testTheater1);
        theaterRepository.save(testTheater2);

        // Verify that the endpoint returns a 200-OK status and correct response body
        mockMvc.perform(get("/theaters")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("direction", "asc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Retrieved 2 Theater(s) (Page 1 of 1)"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(2))
                .andExpect(jsonPath("$.data.content[0].name").value("Integration Test Theater 1"))
                .andExpect(jsonPath("$.data.content[0].location").value("Integration Test Location 1"))
                .andExpect(jsonPath("$.data.content[1].name").value("Integration Test Theater 2"))
                .andExpect(jsonPath("$.data.content[1].location").value("Integration Test Location 2"));
    }

    @Test
    void testGetAllTheatersWithDescendingSort() throws Exception {
        // Set schedules to null to avoid circular references
        testTheater1.setSchedules(null);
        testTheater2.setSchedules(null);
        theaterRepository.save(testTheater1);
        theaterRepository.save(testTheater2);

        // Verify that the endpoint returns a 200-OK status and correct response body
        mockMvc.perform(get("/theaters")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("direction", "desc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Retrieved 2 Theater(s) (Page 1 of 1)"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(2))
                .andExpect(jsonPath("$.data.content[0].name").value("Integration Test Theater 2"))
                .andExpect(jsonPath("$.data.content[0].location").value("Integration Test Location 2"))
                .andExpect(jsonPath("$.data.content[1].name").value("Integration Test Theater 1"))
                .andExpect(jsonPath("$.data.content[1].location").value("Integration Test Location 1"));
    }

    @Test
    void testGetTheatersByMovieId() throws Exception {
        // Set schedules to null to avoid circular references
        testTheater1.setSchedules(null);
        testTheater2.setSchedules(null);
        theaterRepository.save(testTheater1);
        theaterRepository.save(testTheater2);

        mockMvc.perform(get("/theaters/movie/" + testMovie.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print()) // Print the response for debugging
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Found 2 theater(s) showing movie: Integration Test Movie"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].name").value("Integration Test Theater 1"))
                .andExpect(jsonPath("$.data[0].location").value("Integration Test Location 1"))
                .andExpect(jsonPath("$.data[1].name").value("Integration Test Theater 2"))
                .andExpect(jsonPath("$.data[1].location").value("Integration Test Location 2"));
    }

    @Test
    void testGetTheatersByNonExistingMovieId() throws Exception {
        long nonExistentId = 999L;
        mockMvc.perform(get("/theaters/movie/" + nonExistentId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Movie not found"));
    }

    @Test
    void testGetTheatersByMovieIdNoTheaters() throws Exception {
        // Create a movie with no schedules
        Movie movieWithNoSchedules = new Movie();
        movieWithNoSchedules.setTitle("Movie With No Schedules");
        movieWithNoSchedules.setGenre("Drama");
        movieWithNoSchedules.setDuration(90);
        movieWithNoSchedules.setRating(3.5);
        movieWithNoSchedules = movieRepository.save(movieWithNoSchedules);

        mockMvc.perform(get("/theaters/movie/" + movieWithNoSchedules.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("No theaters found showing movie: Movie With No Schedules"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(0)));
    }
}
