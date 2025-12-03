package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Ahoy matey! Test class for our MovieService search functionality.
 * These tests ensure our treasure hunting methods work ship-shape!
 */
@DisplayName("🏴‍☠️ MovieService Search Tests - Treasure Hunting Adventures")
public class MovieServiceSearchTest {

    private MovieService movieService;

    @BeforeEach
    public void setUp() {
        movieService = new MovieService();
    }

    @Test
    @DisplayName("⚓ Test searching by movie name - Find treasure by name")
    public void testSearchMoviesByName_ShouldFindTreasureByName() {
        // Arrange - prepare for the treasure hunt
        String searchName = "Prison";
        
        // Act - hunt for the treasure
        List<Movie> results = movieService.searchMoviesByName(searchName);
        
        // Assert - verify we found the right treasure
        assertNotNull(results, "Arrr! Results should not be null, ye scurvy dog!");
        assertFalse(results.isEmpty(), "Blimey! Should find at least one movie with 'Prison' in the name");
        
        // Verify all results contain the search term
        for (Movie movie : results) {
            assertTrue(movie.getMovieName().toLowerCase().contains(searchName.toLowerCase()),
                "Shiver me timbers! Movie '" + movie.getMovieName() + "' should contain '" + searchName + "'");
        }
    }

    @Test
    @DisplayName("🔍 Test searching by genre - Find movies of same type")
    public void testSearchMoviesByGenre_ShouldFindMoviesOfSameType() {
        // Arrange
        String searchGenre = "Drama";
        
        // Act
        List<Movie> results = movieService.searchMoviesByGenre(searchGenre);
        
        // Assert
        assertNotNull(results, "Results should not be null, matey!");
        assertFalse(results.isEmpty(), "Should find drama movies in our treasure chest");
        
        // Verify all results contain the genre
        for (Movie movie : results) {
            assertTrue(movie.getGenre().toLowerCase().contains(searchGenre.toLowerCase()),
                "Movie '" + movie.getMovieName() + "' should be of genre '" + searchGenre + "'");
        }
    }

    @Test
    @DisplayName("🎯 Test comprehensive search - Multiple criteria treasure hunt")
    public void testSearchMovies_WithMultipleCriteria() {
        // Arrange
        String name = "The";
        String genre = "Drama";
        
        // Act
        List<Movie> results = movieService.searchMovies(name, null, genre);
        
        // Assert
        assertNotNull(results, "Yo ho ho! Results should not be null");
        
        // All results should match both criteria
        for (Movie movie : results) {
            assertTrue(movie.getMovieName().toLowerCase().contains(name.toLowerCase()),
                "Movie name should contain '" + name + "'");
            assertTrue(movie.getGenre().toLowerCase().contains(genre.toLowerCase()),
                "Movie genre should contain '" + genre + "'");
        }
    }

    @Test
    @DisplayName("🆔 Test search by ID - Find specific treasure")
    public void testSearchMovies_ById() {
        // Arrange
        Long searchId = 1L;
        
        // Act
        List<Movie> results = movieService.searchMovies(null, searchId, null);
        
        // Assert
        assertNotNull(results, "Results should not be null, ye landlubber!");
        
        if (!results.isEmpty()) {
            assertEquals(1, results.size(), "Should find exactly one movie by ID");
            assertEquals(searchId, results.get(0).getId(), "Found movie should have the correct ID");
        }
    }

    @Test
    @DisplayName("🏴‍☠️ Test empty search name - Handle scurvy input")
    public void testSearchMoviesByName_WithEmptyName() {
        // Act & Assert
        List<Movie> results = movieService.searchMoviesByName("");
        assertTrue(results.isEmpty(), "Empty name search should return empty treasure chest");
        
        results = movieService.searchMoviesByName(null);
        assertTrue(results.isEmpty(), "Null name search should return empty treasure chest");
        
        results = movieService.searchMoviesByName("   ");
        assertTrue(results.isEmpty(), "Whitespace name search should return empty treasure chest");
    }

    @Test
    @DisplayName("🌊 Test empty search genre - Handle empty criteria")
    public void testSearchMoviesByGenre_WithEmptyGenre() {
        // Act & Assert
        List<Movie> results = movieService.searchMoviesByGenre("");
        assertTrue(results.isEmpty(), "Empty genre search should return empty list");
        
        results = movieService.searchMoviesByGenre(null);
        assertTrue(results.isEmpty(), "Null genre search should return empty list");
        
        results = movieService.searchMoviesByGenre("   ");
        assertTrue(results.isEmpty(), "Whitespace genre search should return empty list");
    }

    @Test
    @DisplayName("🗺️ Test case insensitive search - Pirates don't care about case")
    public void testSearchMovies_CaseInsensitive() {
        // Arrange
        String upperCaseName = "PRISON";
        String lowerCaseName = "prison";
        String mixedCaseName = "PrIsOn";
        
        // Act
        List<Movie> upperResults = movieService.searchMoviesByName(upperCaseName);
        List<Movie> lowerResults = movieService.searchMoviesByName(lowerCaseName);
        List<Movie> mixedResults = movieService.searchMoviesByName(mixedCaseName);
        
        // Assert - all should return the same results
        assertEquals(upperResults.size(), lowerResults.size(), 
            "Upper and lower case searches should return same number of results");
        assertEquals(upperResults.size(), mixedResults.size(), 
            "Mixed case search should return same number of results");
    }

    @Test
    @DisplayName("🏝️ Test search with no matches - Empty treasure island")
    public void testSearchMovies_NoMatches() {
        // Arrange
        String nonExistentName = "NonExistentMovieNameThatShouldNotExist";
        String nonExistentGenre = "NonExistentGenre";
        
        // Act
        List<Movie> nameResults = movieService.searchMoviesByName(nonExistentName);
        List<Movie> genreResults = movieService.searchMoviesByGenre(nonExistentGenre);
        List<Movie> combinedResults = movieService.searchMovies(nonExistentName, null, nonExistentGenre);
        
        // Assert
        assertTrue(nameResults.isEmpty(), "Should return empty list for non-existent movie name");
        assertTrue(genreResults.isEmpty(), "Should return empty list for non-existent genre");
        assertTrue(combinedResults.isEmpty(), "Should return empty list for non-existent criteria");
    }

    @Test
    @DisplayName("🎭 Test get all genres - List all treasure types")
    public void testGetAllGenres_ShouldReturnUniqueGenres() {
        // Act
        List<String> genres = movieService.getAllGenres();
        
        // Assert
        assertNotNull(genres, "Genres list should not be null, matey!");
        assertFalse(genres.isEmpty(), "Should have genres in our treasure chest");
        
        // Check for uniqueness (no duplicates)
        long uniqueCount = genres.stream().distinct().count();
        assertEquals(uniqueCount, genres.size(), "All genres should be unique, no duplicates allowed!");
        
        // Verify genres are sorted
        List<String> sortedGenres = genres.stream().sorted().toList();
        assertEquals(sortedGenres, genres, "Genres should be sorted alphabetically");
    }

    @Test
    @DisplayName("⚔️ Test search with invalid ID - Handle scurvy parameters")
    public void testSearchMovies_WithInvalidId() {
        // Arrange
        Long negativeId = -1L;
        Long zeroId = 0L;
        
        // Act
        List<Movie> negativeResults = movieService.searchMovies(null, negativeId, null);
        List<Movie> zeroResults = movieService.searchMovies(null, zeroId, null);
        
        // Assert
        assertTrue(negativeResults.isEmpty(), "Negative ID should return empty results");
        assertTrue(zeroResults.isEmpty(), "Zero ID should return empty results");
    }

    @Test
    @DisplayName("🌟 Test partial name matching - Find treasure with partial clues")
    public void testSearchMovies_PartialNameMatching() {
        // Arrange
        String partialName = "The";
        
        // Act
        List<Movie> results = movieService.searchMoviesByName(partialName);
        
        // Assert
        assertNotNull(results, "Results should not be null");
        
        // Should find multiple movies starting with "The"
        assertTrue(results.size() > 1, "Should find multiple movies with 'The' in the name");
        
        // Verify all results contain the partial name
        for (Movie movie : results) {
            assertTrue(movie.getMovieName().toLowerCase().contains(partialName.toLowerCase()),
                "Movie '" + movie.getMovieName() + "' should contain '" + partialName + "'");
        }
    }
}