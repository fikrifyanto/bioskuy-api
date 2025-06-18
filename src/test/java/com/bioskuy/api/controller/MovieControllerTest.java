package com.bioskuy.api.controller;

import com.bioskuy.api.model.Movie;
import com.bioskuy.api.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MovieControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MovieService movieService;

    @InjectMocks
    private MovieController movieController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Movie testMovie;
    private List<Movie> testMovies;
    private Page<Movie> testMoviePage;

    @BeforeEach
    void setUp() {
        // Create a test movie
        testMovie = new Movie();
        testMovie.setId(1L);
        testMovie.setTitle("Test Movie");
        testMovie.setGenre("Action");
        testMovie.setDuration(120);
        testMovie.setRating(4.5);

        // Create a test movie list
        Movie movie2 = new Movie();
        movie2.setId(2L);
        movie2.setTitle("Another Movie");
        movie2.setGenre("Comedy");
        movie2.setDuration(90);
        movie2.setRating(3.8);

        testMovies = List.of(testMovie, movie2);
        // Create a properly configured PageImpl with all necessary parameters
        testMoviePage = new PageImpl<>(
            testMovies, 
            PageRequest.of(0, 10, Sort.by("id").ascending()), 
            testMovies.size()
        );

        // Create and configure a message converter
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);

        // Create and configure exception resolver
        ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver();
        exceptionResolver.setMessageConverters(List.of(converter));
        exceptionResolver.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(movieController)
                .setMessageConverters(converter)
                .setHandlerExceptionResolvers(exceptionResolver)
                .setControllerAdvice(new com.bioskuy.api.exception.GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllMovies_NoFilters_ReturnsAllMovies() throws Exception {
        // Mock service response for no filters
        when(movieService.getMoviesByFilters(any(), any(), any(), any()))
                .thenReturn(testMoviePage);

        mockMvc.perform(get("/movies")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "id")
                .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Retrieved 2 Movie(s) (Page 1 of 1)"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(2))
                .andExpect(jsonPath("$.data.content[0].id").value(1))
                .andExpect(jsonPath("$.data.content[0].title").value("Test Movie"))
                .andExpect(jsonPath("$.data.content[0].genre").value("Action"))
                .andExpect(jsonPath("$.data.content[0].duration").value(120))
                .andExpect(jsonPath("$.data.content[0].rating").value(4.5))
                .andExpect(jsonPath("$.data.content[1].id").value(2))
                .andExpect(jsonPath("$.data.content[1].title").value("Another Movie"));
    }

    @Test
    void getAllMovies_WithFilters_ReturnsFilteredMovies() throws Exception {
        // Create a filtered page with only the first movie
        Page<Movie> filteredPage = new PageImpl<>(
            List.of(testMovie),
            PageRequest.of(0, 10, Sort.by("id").ascending()),
            1
        );

        // Mock service response for title filter
        when(movieService.getMoviesByFilters(any(), any(), any(), any()))
                .thenReturn(filteredPage);

        mockMvc.perform(get("/movies")
                .param("page", "0")
                .param("size", "10")
                .param("title", "Test")
                .param("sortBy", "id")
                .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Retrieved 1 Movie(s) (Page 1 of 1)"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.content[0].id").value(1))
                .andExpect(jsonPath("$.data.content[0].title").value("Test Movie"));
    }

    @Test
    void getAllMovies_WithGenreFilter_ReturnsFilteredMovies() throws Exception {
        // Create a filtered page with only the first movie
        Page<Movie> filteredPage = new PageImpl<>(
            List.of(testMovie),
            PageRequest.of(0, 10, Sort.by("id").ascending()),
            1
        );

        // Mock service response for genre filter
        when(movieService.getMoviesByFilters(any(), any(), any(), any()))
                .thenReturn(filteredPage);

        mockMvc.perform(get("/movies")
                .param("page", "0")
                .param("size", "10")
                .param("genre", "Action")
                .param("sortBy", "id")
                .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Retrieved 1 Movie(s) (Page 1 of 1)"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.content[0].genre").value("Action"));
    }

    @Test
    void getAllMovies_WithRatingFilter_ReturnsFilteredMovies() throws Exception {
        // Create a filtered page with only the first movie
        Page<Movie> filteredPage = new PageImpl<>(
            List.of(testMovie),
            PageRequest.of(0, 10, Sort.by("id").ascending()),
            1
        );

        // Mock service response for rating filter
        when(movieService.getMoviesByFilters(any(), any(), any(), any()))
                .thenReturn(filteredPage);

        mockMvc.perform(get("/movies")
                .param("page", "0")
                .param("size", "10")
                .param("rating", "4.5")
                .param("sortBy", "id")
                .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Retrieved 1 Movie(s) (Page 1 of 1)"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.content[0].rating").value(4.5));
    }

    @Test
    void getAllMovies_WithDescendingSort_ReturnsSortedMovies() throws Exception {
        // Create a page with reversed order
        Page<Movie> reversedPage = new PageImpl<>(
            List.of(testMovies.get(1), testMovies.get(0)),
            PageRequest.of(0, 10, Sort.by("id").descending()),
            2
        );

        // Mock service response for descending sort
        when(movieService.getMoviesByFilters(any(), any(), any(), any()))
                .thenReturn(reversedPage);

        mockMvc.perform(get("/movies")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "id")
                .param("direction", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Retrieved 2 Movie(s) (Page 1 of 1)"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(2))
                .andExpect(jsonPath("$.data.content[0].id").value(2))
                .andExpect(jsonPath("$.data.content[1].id").value(1));
    }

    @Test
    void getMovieById_ExistingId_ReturnsMovie() throws Exception {
        when(movieService.getMovieById(1L)).thenReturn(testMovie);

        mockMvc.perform(get("/movies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Found movie with ID 1"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Test Movie"))
                .andExpect(jsonPath("$.data.genre").value("Action"))
                .andExpect(jsonPath("$.data.duration").value(120))
                .andExpect(jsonPath("$.data.rating").value(4.5));
    }

    @Test
    void getMovieById_NonExistingId_ReturnsNotFound() throws Exception {
        when(movieService.getMovieById(999L))
                .thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Movie not found"));

        mockMvc.perform(get("/movies/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Movie not found"));
    }
}
