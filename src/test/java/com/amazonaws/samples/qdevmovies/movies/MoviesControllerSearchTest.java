package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ExtendedModelMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Ahoy matey! Test class for our MoviesController search functionality.
 * These tests ensure our search endpoints work like a well-oiled pirate ship!
 */
@DisplayName("🏴‍☠️ MoviesController Search Tests - Navigation & Treasure Discovery")
public class MoviesControllerSearchTest {

    private MoviesController moviesController;
    private Model model;
    private MovieService mockMovieService;
    private ReviewService mockReviewService;

    @BeforeEach
    public void setUp() {
        moviesController = new MoviesController();
        model = new ExtendedModelMap();
        
        // Create mock services with pirate-themed test data
        mockMovieService = new MovieService() {
            @Override
            public List<Movie> getAllMovies() {
                return Arrays.asList(
                    new Movie(1L, "The Pirate's Treasure", "Captain Hook", 2023, "Adventure", "A pirate's quest for gold", 120, 4.5),
                    new Movie(2L, "Sea Battle", "Admiral Blackbeard", 2022, "Action", "Epic naval warfare", 140, 4.0),
                    new Movie(3L, "Landlubber's Tale", "Sailor Sam", 2021, "Drama", "A story of land and sea", 110, 3.5)
                );
            }
            
            @Override
            public List<Movie> searchMovies(String name, Long id, String genre) {
                List<Movie> allMovies = getAllMovies();
                List<Movie> results = new ArrayList<>();
                
                for (Movie movie : allMovies) {
                    boolean matches = true;
                    
                    if (name != null && !name.trim().isEmpty()) {
                        matches = matches && movie.getMovieName().toLowerCase().contains(name.toLowerCase());
                    }
                    
                    if (id != null && id > 0) {
                        matches = matches && movie.getId().equals(id);
                    }
                    
                    if (genre != null && !genre.trim().isEmpty()) {
                        matches = matches && movie.getGenre().toLowerCase().contains(genre.toLowerCase());
                    }
                    
                    if (matches) {
                        results.add(movie);
                    }
                }
                
                return results;
            }
            
            @Override
            public List<String> getAllGenres() {
                return Arrays.asList("Action", "Adventure", "Drama");
            }
        };
        
        mockReviewService = new ReviewService() {
            @Override
            public List<Review> getReviewsForMovie(long movieId) {
                return new ArrayList<>();
            }
        };
        
        // Inject mocks using reflection
        try {
            java.lang.reflect.Field movieServiceField = MoviesController.class.getDeclaredField("movieService");
            movieServiceField.setAccessible(true);
            movieServiceField.set(moviesController, mockMovieService);
            
            java.lang.reflect.Field reviewServiceField = MoviesController.class.getDeclaredField("reviewService");
            reviewServiceField.setAccessible(true);
            reviewServiceField.set(moviesController, mockReviewService);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mock services", e);
        }
    }

    @Test
    @DisplayName("⚓ Test search by name - Find treasure by name")
    public void testSearchMovies_ByName_ShouldFindTreasure() {
        // Arrange
        String searchName = "Pirate";
        
        // Act
        String result = moviesController.searchMovies(searchName, null, null, model);
        
        // Assert
        assertEquals("movies", result, "Should return movies template");
        assertTrue(model.containsAttribute("movies"), "Model should contain movies attribute");
        assertTrue(model.containsAttribute("searchPerformed"), "Model should indicate search was performed");
        assertTrue(model.containsAttribute("searchMessage"), "Model should contain search message");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertNotNull(movies, "Movies list should not be null");
        assertFalse(movies.isEmpty(), "Should find movies with 'Pirate' in name");
        
        // Verify search message contains pirate language
        String searchMessage = (String) model.getAttribute("searchMessage");
        assertTrue(searchMessage.contains("Yo ho ho!") || searchMessage.contains("found"), 
            "Search message should contain pirate language or success indication");
    }

    @Test
    @DisplayName("🆔 Test search by ID - Find specific treasure")
    public void testSearchMovies_ById_ShouldFindSpecificTreasure() {
        // Arrange
        Long searchId = 1L;
        
        // Act
        String result = moviesController.searchMovies(null, searchId, null, model);
        
        // Assert
        assertEquals("movies", result, "Should return movies template");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertNotNull(movies, "Movies list should not be null");
        assertEquals(1, movies.size(), "Should find exactly one movie by ID");
        assertEquals(searchId, movies.get(0).getId(), "Found movie should have correct ID");
    }

    @Test
    @DisplayName("🎭 Test search by genre - Find movies of same type")
    public void testSearchMovies_ByGenre_ShouldFindMoviesOfSameType() {
        // Arrange
        String searchGenre = "Adventure";
        
        // Act
        String result = moviesController.searchMovies(null, null, searchGenre, model);
        
        // Assert
        assertEquals("movies", result, "Should return movies template");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertNotNull(movies, "Movies list should not be null");
        
        // Verify all found movies match the genre
        for (Movie movie : movies) {
            assertTrue(movie.getGenre().toLowerCase().contains(searchGenre.toLowerCase()),
                "All movies should match the search genre");
        }
    }

    @Test
    @DisplayName("🏴‍☠️ Test search with no parameters - Handle scurvy input")
    public void testSearchMovies_NoParameters_ShouldShowWarning() {
        // Act
        String result = moviesController.searchMovies(null, null, null, model);
        
        // Assert
        assertEquals("movies", result, "Should return movies template");
        assertTrue(model.containsAttribute("searchMessage"), "Should contain warning message");
        
        String searchMessage = (String) model.getAttribute("searchMessage");
        assertTrue(searchMessage.contains("Arrr!") && searchMessage.contains("search criteria"),
            "Should contain pirate warning about missing search criteria");
        
        // Should show all movies when no search criteria provided
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertEquals(3, movies.size(), "Should show all movies when no search criteria");
    }

    @Test
    @DisplayName("🏝️ Test search with no results - Empty treasure island")
    public void testSearchMovies_NoResults_ShouldShowEmptyMessage() {
        // Arrange
        String nonExistentName = "NonExistentTreasure";
        
        // Act
        String result = moviesController.searchMovies(nonExistentName, null, null, model);
        
        // Assert
        assertEquals("movies", result, "Should return movies template");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertTrue(movies.isEmpty(), "Should return empty results for non-existent movie");
        
        String searchMessage = (String) model.getAttribute("searchMessage");
        assertTrue(searchMessage.contains("Shiver me timbers!") || searchMessage.contains("No movies found"),
            "Should contain pirate message about no results");
    }

    @Test
    @DisplayName("🌟 Test API search endpoint - JSON treasure map")
    public void testSearchMoviesApi_ShouldReturnJsonTreasureMap() {
        // Arrange
        String searchName = "Pirate";
        
        // Act
        ResponseEntity<Map<String, Object>> response = moviesController.searchMoviesApi(searchName, null, null);
        
        // Assert
        assertNotNull(response, "Response should not be null");
        assertEquals(200, response.getStatusCodeValue(), "Should return HTTP 200 OK");
        
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        
        assertTrue((Boolean) responseBody.get("success"), "API response should indicate success");
        assertTrue(responseBody.containsKey("movies"), "Response should contain movies");
        assertTrue(responseBody.containsKey("totalResults"), "Response should contain total results");
        assertTrue(responseBody.containsKey("message"), "Response should contain message");
        
        String message = (String) responseBody.get("message");
        assertTrue(message.contains("Found") && message.contains("treasure chest"),
            "API message should contain pirate language");
    }

    @Test
    @DisplayName("⚔️ Test API search with no parameters - Handle scurvy API request")
    public void testSearchMoviesApi_NoParameters_ShouldReturnBadRequest() {
        // Act
        ResponseEntity<Map<String, Object>> response = moviesController.searchMoviesApi(null, null, null);
        
        // Assert
        assertNotNull(response, "Response should not be null");
        assertEquals(400, response.getStatusCodeValue(), "Should return HTTP 400 Bad Request");
        
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        
        assertFalse((Boolean) responseBody.get("success"), "API response should indicate failure");
        
        String message = (String) responseBody.get("message");
        assertTrue(message.contains("Arrr!") && message.contains("required"),
            "API error message should contain pirate language");
    }

    @Test
    @DisplayName("🗺️ Test API search with no results - Empty treasure map")
    public void testSearchMoviesApi_NoResults_ShouldReturnEmptyResults() {
        // Arrange
        String nonExistentName = "NonExistentTreasure";
        
        // Act
        ResponseEntity<Map<String, Object>> response = moviesController.searchMoviesApi(nonExistentName, null, null);
        
        // Assert
        assertEquals(200, response.getStatusCodeValue(), "Should return HTTP 200 OK even with no results");
        
        Map<String, Object> responseBody = response.getBody();
        assertTrue((Boolean) responseBody.get("success"), "Should still be successful even with no results");
        assertEquals(0, responseBody.get("totalResults"), "Total results should be 0");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) responseBody.get("movies");
        assertTrue(movies.isEmpty(), "Movies list should be empty");
        
        String message = (String) responseBody.get("message");
        assertTrue(message.contains("No movies found") && message.contains("scurvy"),
            "Message should contain pirate language about no results");
    }

    @Test
    @DisplayName("🎯 Test search parameters preservation - Keep treasure map coordinates")
    public void testSearchMovies_ShouldPreserveSearchParameters() {
        // Arrange
        String searchName = "Pirate";
        Long searchId = 1L;
        String searchGenre = "Adventure";
        
        // Act
        moviesController.searchMovies(searchName, searchId, searchGenre, model);
        
        // Assert - verify search parameters are preserved in model
        assertEquals(searchName, model.getAttribute("searchName"), "Search name should be preserved");
        assertEquals(searchId, model.getAttribute("searchId"), "Search ID should be preserved");
        assertEquals(searchGenre, model.getAttribute("searchGenre"), "Search genre should be preserved");
    }

    @Test
    @DisplayName("🌊 Test genres are included in response - Show available treasure types")
    public void testSearchMovies_ShouldIncludeGenres() {
        // Act
        moviesController.searchMovies("Pirate", null, null, model);
        
        // Assert
        assertTrue(model.containsAttribute("genres"), "Model should contain genres for dropdown");
        
        @SuppressWarnings("unchecked")
        List<String> genres = (List<String>) model.getAttribute("genres");
        assertNotNull(genres, "Genres list should not be null");
        assertFalse(genres.isEmpty(), "Genres list should not be empty");
        assertTrue(genres.contains("Adventure"), "Should contain Adventure genre");
    }

    @Test
    @DisplayName("🏴‍☠️ Test API search parameters in response - Include treasure map legend")
    public void testSearchMoviesApi_ShouldIncludeSearchParameters() {
        // Arrange
        String searchName = "Pirate";
        String searchGenre = "Adventure";
        
        // Act
        ResponseEntity<Map<String, Object>> response = moviesController.searchMoviesApi(searchName, null, searchGenre);
        
        // Assert
        Map<String, Object> responseBody = response.getBody();
        assertTrue(responseBody.containsKey("searchParameters"), "Response should include search parameters");
        
        @SuppressWarnings("unchecked")
        Map<String, Object> searchParams = (Map<String, Object>) responseBody.get("searchParameters");
        assertEquals(searchName, searchParams.get("name"), "Should include search name parameter");
        assertEquals(searchGenre, searchParams.get("genre"), "Should include search genre parameter");
    }
}