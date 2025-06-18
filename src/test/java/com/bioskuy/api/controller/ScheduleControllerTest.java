package com.bioskuy.api.controller;

import com.bioskuy.api.model.Movie;
import com.bioskuy.api.model.Schedule;
import com.bioskuy.api.model.Theater;
import com.bioskuy.api.service.ScheduleService;
import com.bioskuy.api.service.TheaterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ScheduleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private TheaterService theaterService;

    @InjectMocks
    private ScheduleController scheduleController;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private List<Schedule> testSchedules;
    private Page<Schedule> testSchedulePage;
    private Theater testTheater;

    @BeforeEach
    void setUp() {
        // Create a test movie
        Movie testMovie = new Movie();
        testMovie.setId(1L);
        testMovie.setTitle("Test Movie");
        testMovie.setGenre("Action");
        testMovie.setDuration(120);
        testMovie.setRating(4.5);

        // Create a test theater
        testTheater = new Theater();
        testTheater.setId(1L);
        testTheater.setName("Test Theater");
        testTheater.setLocation("Test Location");

        // Create a test schedule
        Schedule testSchedule = new Schedule();
        testSchedule.setId(1L);
        testSchedule.setMovie(testMovie);
        testSchedule.setTheater(testTheater);
        testSchedule.setShowingDate(LocalDate.now());
        testSchedule.setShowingTime(LocalTime.of(14, 0));
        testSchedule.setTicketPrice(50.0);

        // Create a second test schedule
        Schedule schedule2 = new Schedule();
        schedule2.setId(2L);
        schedule2.setMovie(testMovie);
        schedule2.setTheater(testTheater);
        schedule2.setShowingDate(LocalDate.now().plusDays(1));
        schedule2.setShowingTime(LocalTime.of(16, 0));
        schedule2.setTicketPrice(60.0);

        testSchedules = List.of(testSchedule, schedule2);

        // Create a properly configured PageImpl with all necessary parameters
        testSchedulePage = new PageImpl<>(
            testSchedules, 
            PageRequest.of(0, 10, Sort.by("id").ascending()), 
            testSchedules.size()
        );

        // Create and configure a message converter
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);

        // Create and configure exception resolver
        ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver();
        exceptionResolver.setMessageConverters(List.of(converter));
        exceptionResolver.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(scheduleController)
                .setMessageConverters(converter)
                .setHandlerExceptionResolvers(exceptionResolver)
                .setControllerAdvice(new com.bioskuy.api.exception.GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllSchedules_ReturnsAllSchedules() throws Exception {
        // Mock service response
        when(scheduleService.getAllSchedule(any(PageRequest.class)))
                .thenReturn(testSchedulePage);

        mockMvc.perform(get("/schedules")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "id")
                .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Retrieved 2 Schedule(s) (Page 1 of 1)"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(2))
                .andExpect(jsonPath("$.data.content[0].id").value(1))
                .andExpect(jsonPath("$.data.content[0].movie.title").value("Test Movie"))
                .andExpect(jsonPath("$.data.content[0].showingDate").isNotEmpty())
                .andExpect(jsonPath("$.data.content[0].showingTime").isNotEmpty())
                .andExpect(jsonPath("$.data.content[0].ticketPrice").value(50.0))
                .andExpect(jsonPath("$.data.content[1].id").value(2))
                .andExpect(jsonPath("$.data.content[1].ticketPrice").value(60.0));
    }

    @Test
    void getAllSchedules_WithDescendingSort_ReturnsSortedSchedules() throws Exception {
        // Create a page with reversed order
        Page<Schedule> reversedPage = new PageImpl<>(
            List.of(testSchedules.get(1), testSchedules.get(0)),
            PageRequest.of(0, 10, Sort.by("id").descending()),
            2
        );

        // Mock service response for descending sort
        when(scheduleService.getAllSchedule(any(PageRequest.class)))
                .thenReturn(reversedPage);

        mockMvc.perform(get("/schedules")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "id")
                .param("direction", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Retrieved 2 Schedule(s) (Page 1 of 1)"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(2))
                .andExpect(jsonPath("$.data.content[0].id").value(2))
                .andExpect(jsonPath("$.data.content[1].id").value(1));
    }

    @Test
    void getSchedulesByTheaterId_ExistingId_ReturnsSchedules() throws Exception {
        // Mock theater service response
        when(theaterService.getTheaterbyId(1L)).thenReturn(testTheater);

        // Mock schedule service response
        when(scheduleService.getAllSchedulebyTheater(eq(testTheater), any(PageRequest.class)))
                .thenReturn(testSchedulePage);

        mockMvc.perform(get("/schedules/theater/1")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "id")
                .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Retrieved 2 Schedule(s) From Theater Test Theater (Page 1 of 1)"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(2))
                .andExpect(jsonPath("$.data.content[0].id").value(1))
                .andExpect(jsonPath("$.data.content[0].movie.title").value("Test Movie"));
    }

    @Test
    void getSchedulesByTheaterId_NonExistingId_ReturnsNotFound() throws Exception {
        // Mock theater service to throw exception for non-existing theater
        when(theaterService.getTheaterbyId(999L))
                .thenThrow(new IllegalArgumentException("Theater with ID 999 not found"));

        mockMvc.perform(get("/schedules/theater/999")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "id")
                .param("direction", "asc"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Theater with ID 999 not found"));
    }
}
