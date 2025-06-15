package com.bioskuy.api.controller;

import com.bioskuy.api.model.Movie;
import com.bioskuy.api.model.Theater;
import com.bioskuy.api.service.MovieService;
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
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TheaterControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TheaterService theaterService;

    @Mock
    private MovieService movieService;

    @InjectMocks
    private TheaterController theaterController;

    private Movie testMovie;
    private List<Theater> testTheaters;
    private Page<Theater> testTheaterPage;

    @BeforeEach
    void setUp() {
        // Configure ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .configure(com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Create test theaters
        Theater testTheater1 = new Theater();
        testTheater1.setId(1L);
        testTheater1.setName("Test Theater 1");
        testTheater1.setLocation("Test Location 1");
        testTheater1.setSchedules(new ArrayList<>()); // Initialize with an empty list to avoid NPE

        Theater testTheater2 = new Theater();
        testTheater2.setId(2L);
        testTheater2.setName("Test Theater 2");
        testTheater2.setLocation("Test Location 2");
        testTheater2.setSchedules(new ArrayList<>()); // Initialize with an empty list to avoid NPE

        // Create a test movie
        testMovie = new Movie();
        testMovie.setId(1L);
        testMovie.setTitle("Test Movie");
        testMovie.setGenre("Action");
        testMovie.setDuration(120);
        testMovie.setRating(4.5);

        // Create a test theater list
        testTheaters = List.of(testTheater1, testTheater2);

        // Create a properly configured PageImpl with all necessary parameters
        testTheaterPage = new PageImpl<>(
            testTheaters, 
            PageRequest.of(0, 10, Sort.by("id").ascending()), 
            testTheaters.size()
        );

        // Create and configure a message converter
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);

        // Create and configure exception resolver
        ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver();
        exceptionResolver.setMessageConverters(List.of(converter));
        exceptionResolver.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(theaterController)
                .setMessageConverters(converter)
                .setHandlerExceptionResolvers(exceptionResolver)
                .setControllerAdvice(new com.bioskuy.api.exception.GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllTheaters_ReturnsAllTheaters() throws Exception {
        // Mock service response
        when(theaterService.getTheatersPaginated(any()))
                .thenReturn(testTheaterPage);

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
                .andExpect(jsonPath("$.data.content[0].id").value(1))
                .andExpect(jsonPath("$.data.content[0].name").value("Test Theater 1"))
                .andExpect(jsonPath("$.data.content[0].location").value("Test Location 1"))
                .andExpect(jsonPath("$.data.content[1].id").value(2))
                .andExpect(jsonPath("$.data.content[1].name").value("Test Theater 2"))
                .andExpect(jsonPath("$.data.content[1].location").value("Test Location 2"));
    }

    @Test
    void getAllTheaters_WithDescendingSort_ReturnsSortedTheaters() throws Exception {
        // Create a page with reversed order
        Page<Theater> reversedPage = new PageImpl<>(
            List.of(testTheaters.get(1), testTheaters.get(0)),
            PageRequest.of(0, 10, Sort.by("id").descending()),
            2
        );

        // Mock service response for descending sort
        when(theaterService.getTheatersPaginated(any()))
                .thenReturn(reversedPage);

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
                .andExpect(jsonPath("$.data.content[0].id").value(2))
                .andExpect(jsonPath("$.data.content[1].id").value(1));
    }

    @Test
    void getTheatersByMovieId_ExistingMovieId_ReturnsTheaters() throws Exception {
        // Mock service responses
        when(movieService.getMovieById(1L)).thenReturn(testMovie);
        when(theaterService.getTheatersByMovieId(1L)).thenReturn(testTheaters);

        mockMvc.perform(get("/theaters/movie/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Found 2 theater(s) showing movie: Test Movie"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("Test Theater 1"))
                .andExpect(jsonPath("$.data[0].location").value("Test Location 1"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].name").value("Test Theater 2"))
                .andExpect(jsonPath("$.data[1].location").value("Test Location 2"));
    }

    @Test
    void getTheatersByMovieId_ExistingMovieIdNoTheaters_ReturnsEmptyList() throws Exception {
        // Mock service responses
        when(movieService.getMovieById(1L)).thenReturn(testMovie);
        when(theaterService.getTheatersByMovieId(1L)).thenReturn(List.of());

        mockMvc.perform(get("/theaters/movie/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("No theaters found showing movie: Test Movie"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));
    }

    @Test
    void getTheatersByMovieId_NonExistingMovieId_ReturnsNotFound() throws Exception {
        // Mock service response for non-existing movie
        when(movieService.getMovieById(999L))
                .thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Movie not found"));

        mockMvc.perform(get("/theaters/movie/999")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Movie not found"));
    }
}