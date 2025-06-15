package com.bioskuy.api.controller;

import com.bioskuy.api.model.Movie;
import com.bioskuy.api.repository.MovieRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MovieControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    private Movie testMovie1;

    @BeforeEach
    void setUp() {
        // Clean up the database before each test
        movieRepository.deleteAll();

        // Create test movies
        testMovie1 = new Movie();
        testMovie1.setTitle("Integration Test Movie");
        testMovie1.setGenre("Action");
        testMovie1.setDuration(120);
        testMovie1.setRating(4.5);

        Movie testMovie2 = new Movie();
        testMovie2.setTitle("Another Test Movie");
        testMovie2.setGenre("Comedy");
        testMovie2.setDuration(90);
        testMovie2.setRating(3.8);

        // Save the movies to the database
        testMovie1 = movieRepository.save(testMovie1);
        movieRepository.save(testMovie2);
    }

    @AfterEach
    void tearDown() {
        // Clean up the database after each test
        movieRepository.deleteAll();
    }

    @Test
    void testGetAllMovies() throws Exception {
        mockMvc.perform(get("/movies")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Retrieved 2 Movie(s) (Page 1 of 1)"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content", hasSize(2)))
                .andExpect(jsonPath("$.data.content[0].title").value("Integration Test Movie"))
                .andExpect(jsonPath("$.data.content[0].genre").value("Action"))
                .andExpect(jsonPath("$.data.content[0].duration").value(120))
                .andExpect(jsonPath("$.data.content[0].rating").value(4.5))
                .andExpect(jsonPath("$.data.content[1].title").value("Another Test Movie"))
                .andExpect(jsonPath("$.data.content[1].genre").value("Comedy"))
                .andExpect(jsonPath("$.data.content[1].duration").value(90))
                .andExpect(jsonPath("$.data.content[1].rating").value(3.8));
    }

    @Test
    void testGetAllMoviesWithTitleFilter() throws Exception {
        mockMvc.perform(get("/movies")
                        .param("page", "0")
                        .param("size", "10")
                        .param("title", "Integration")
                        .param("sortBy", "id")
                        .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Retrieved 1 Movie(s) (Page 1 of 1)"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content", hasSize(1)))
                .andExpect(jsonPath("$.data.content[0].title").value("Integration Test Movie"));
    }

    @Test
    void testGetAllMoviesWithGenreFilter() throws Exception {
        mockMvc.perform(get("/movies")
                        .param("page", "0")
                        .param("size", "10")
                        .param("genre", "Comedy")
                        .param("sortBy", "id")
                        .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Retrieved 1 Movie(s) (Page 1 of 1)"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content", hasSize(1)))
                .andExpect(jsonPath("$.data.content[0].title").value("Another Test Movie"))
                .andExpect(jsonPath("$.data.content[0].genre").value("Comedy"));
    }

    @Test
    void testGetAllMoviesWithRatingFilter() throws Exception {
        mockMvc.perform(get("/movies")
                        .param("page", "0")
                        .param("size", "10")
                        .param("rating", "4.5")
                        .param("sortBy", "id")
                        .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Retrieved 1 Movie(s) (Page 1 of 1)"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content", hasSize(1)))
                .andExpect(jsonPath("$.data.content[0].title").value("Integration Test Movie"))
                .andExpect(jsonPath("$.data.content[0].rating").value(4.5));
    }

    @Test
    void testGetAllMoviesWithDescendingSort() throws Exception {
        mockMvc.perform(get("/movies")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("direction", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Retrieved 2 Movie(s) (Page 1 of 1)"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content", hasSize(2)))
                .andExpect(jsonPath("$.data.content[0].title").value("Another Test Movie"))
                .andExpect(jsonPath("$.data.content[1].title").value("Integration Test Movie"));
    }

    @Test
    void testGetMovieById() throws Exception {
        mockMvc.perform(get("/movies/" + testMovie1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Found movie with ID " + testMovie1.getId()))
                .andExpect(jsonPath("$.data.id").value(testMovie1.getId()))
                .andExpect(jsonPath("$.data.title").value("Integration Test Movie"))
                .andExpect(jsonPath("$.data.genre").value("Action"))
                .andExpect(jsonPath("$.data.duration").value(120))
                .andExpect(jsonPath("$.data.rating").value(4.5));
    }

    @Test
    void testGetMovieByIdNotFound() throws Exception {
        long nonExistentId = 999L;
        mockMvc.perform(get("/movies/" + nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Movie not found"));
    }
}
