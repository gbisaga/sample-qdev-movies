package com.amazonaws.samples.qdevmovies.movies;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private static final Logger logger = LogManager.getLogger(MovieService.class);
    private final List<Movie> movies;
    private final Map<Long, Movie> movieMap;

    public MovieService() {
        this.movies = loadMoviesFromJson();
        this.movieMap = new HashMap<>();
        for (Movie movie : movies) {
            movieMap.put(movie.getId(), movie);
        }
    }

    private List<Movie> loadMoviesFromJson() {
        List<Movie> movieList = new ArrayList<>();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("movies.json");
            if (inputStream != null) {
                Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
                String jsonContent = scanner.useDelimiter("\\A").next();
                scanner.close();
                
                JSONArray moviesArray = new JSONArray(jsonContent);
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject movieObj = moviesArray.getJSONObject(i);
                    movieList.add(new Movie(
                        movieObj.getLong("id"),
                        movieObj.getString("movieName"),
                        movieObj.getString("director"),
                        movieObj.getInt("year"),
                        movieObj.getString("genre"),
                        movieObj.getString("description"),
                        movieObj.getInt("duration"),
                        movieObj.getDouble("imdbRating")
                    ));
                }
            }
        } catch (Exception e) {
            logger.error("Failed to load movies from JSON: {}", e.getMessage());
        }
        return movieList;
    }

    public List<Movie> getAllMovies() {
        return movies;
    }

    public Optional<Movie> getMovieById(Long id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }
        return Optional.ofNullable(movieMap.get(id));
    }

    /**
     * Ahoy matey! Search through our treasure chest of movies using various criteria.
     * This method be the main search crew member that coordinates all search operations.
     * 
     * @param name The movie name to search for (partial matching, case-insensitive)
     * @param id The exact movie ID to find
     * @param genre The genre to filter by (case-insensitive)
     * @return List of movies matching the search criteria, or empty list if no treasure found
     */
    public List<Movie> searchMovies(String name, Long id, String genre) {
        logger.info("Ahoy! Starting movie search with name: {}, id: {}, genre: {}", name, id, genre);
        
        List<Movie> searchResults = new ArrayList<>(movies);
        
        // Filter by ID first - if provided, it be the most specific search, arrr!
        if (id != null && id > 0) {
            Optional<Movie> movieById = getMovieById(id);
            searchResults = movieById.map(List::of).orElse(new ArrayList<>());
            logger.info("Filtered by ID {}, found {} movies", id, searchResults.size());
        }
        
        // Filter by name - search for partial matches like a true pirate hunting treasure!
        if (name != null && !name.trim().isEmpty()) {
            String searchName = name.trim().toLowerCase();
            searchResults = searchResults.stream()
                .filter(movie -> movie.getMovieName().toLowerCase().contains(searchName))
                .collect(Collectors.toList());
            logger.info("Filtered by name '{}', found {} movies", name, searchResults.size());
        }
        
        // Filter by genre - find movies of the same type, ye scurvy dog!
        if (genre != null && !genre.trim().isEmpty()) {
            String searchGenre = genre.trim().toLowerCase();
            searchResults = searchResults.stream()
                .filter(movie -> movie.getGenre().toLowerCase().contains(searchGenre))
                .collect(Collectors.toList());
            logger.info("Filtered by genre '{}', found {} movies", genre, searchResults.size());
        }
        
        logger.info("Search complete! Found {} movies in our treasure chest", searchResults.size());
        return searchResults;
    }

    /**
     * Arrr! Search movies by name only - for when ye know what treasure ye be seeking!
     * 
     * @param name The movie name to search for (partial matching, case-insensitive)
     * @return List of movies with names containing the search term
     */
    public List<Movie> searchMoviesByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            logger.warn("Blimey! Empty name provided for search, returning empty treasure chest");
            return new ArrayList<>();
        }
        
        String searchName = name.trim().toLowerCase();
        List<Movie> results = movies.stream()
            .filter(movie -> movie.getMovieName().toLowerCase().contains(searchName))
            .collect(Collectors.toList());
            
        logger.info("Name search for '{}' found {} movies", name, results.size());
        return results;
    }

    /**
     * Shiver me timbers! Search movies by genre - find all movies of a particular type!
     * 
     * @param genre The genre to filter by (case-insensitive)
     * @return List of movies matching the genre
     */
    public List<Movie> searchMoviesByGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            logger.warn("Batten down the hatches! Empty genre provided, returning empty list");
            return new ArrayList<>();
        }
        
        String searchGenre = genre.trim().toLowerCase();
        List<Movie> results = movies.stream()
            .filter(movie -> movie.getGenre().toLowerCase().contains(searchGenre))
            .collect(Collectors.toList());
            
        logger.info("Genre search for '{}' found {} movies", genre, results.size());
        return results;
    }

    /**
     * Yo ho ho! Get all unique genres from our movie treasure chest!
     * Useful for showing available genres to search, me hearty!
     * 
     * @return List of unique genres available in the movie collection
     */
    public List<String> getAllGenres() {
        List<String> genres = movies.stream()
            .map(Movie::getGenre)
            .distinct()
            .sorted()
            .collect(Collectors.toList());
            
        logger.info("Found {} unique genres in our treasure chest", genres.size());
        return genres;
    }
}
