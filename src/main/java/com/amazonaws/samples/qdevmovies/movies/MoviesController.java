package com.amazonaws.samples.qdevmovies.movies;

import com.amazonaws.samples.qdevmovies.utils.MovieIconUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class MoviesController {
    private static final Logger logger = LogManager.getLogger(MoviesController.class);

    @Autowired
    private MovieService movieService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/movies")
    public String getMovies(org.springframework.ui.Model model) {
        logger.info("Fetching movies");
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("genres", movieService.getAllGenres()); // Add genres for search dropdown
        return "movies";
    }

    /**
     * Ahoy matey! This be the main search endpoint that handles both HTML form submissions
     * and JSON API requests. It be as versatile as a pirate's cutlass!
     * 
     * @param name Movie name to search for (optional)
     * @param id Movie ID to search for (optional) 
     * @param genre Genre to filter by (optional)
     * @param model Spring model for HTML responses
     * @return JSON response for API calls or HTML page for browser requests
     */
    @GetMapping("/movies/search")
    public String searchMovies(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "genre", required = false) String genre,
            org.springframework.ui.Model model) {
        
        logger.info("Ahoy! Movie search requested with name: {}, id: {}, genre: {}", name, id, genre);
        
        try {
            // Validate that at least one search parameter is provided, ye scurvy dog!
            if ((name == null || name.trim().isEmpty()) && 
                id == null && 
                (genre == null || genre.trim().isEmpty())) {
                
                logger.warn("Blimey! No search parameters provided, showing all movies");
                model.addAttribute("movies", movieService.getAllMovies());
                model.addAttribute("genres", movieService.getAllGenres());
                model.addAttribute("searchMessage", "Arrr! Ye need to provide search criteria, matey!");
                model.addAttribute("searchPerformed", true);
                return "movies";
            }
            
            // Perform the search using our trusty MovieService crew member
            List<Movie> searchResults = movieService.searchMovies(name, id, genre);
            
            // Prepare the response with pirate flair!
            model.addAttribute("movies", searchResults);
            model.addAttribute("genres", movieService.getAllGenres());
            model.addAttribute("searchPerformed", true);
            
            if (searchResults.isEmpty()) {
                model.addAttribute("searchMessage", 
                    "Shiver me timbers! No movies found matching yer search criteria. " +
                    "Try different search terms, ye landlubber!");
                logger.info("No movies found for search criteria");
            } else {
                model.addAttribute("searchMessage", 
                    String.format("Yo ho ho! Found %d movies in our treasure chest!", searchResults.size()));
                logger.info("Search successful, found {} movies", searchResults.size());
            }
            
            // Preserve search parameters for the form
            model.addAttribute("searchName", name);
            model.addAttribute("searchId", id);
            model.addAttribute("searchGenre", genre);
            
            return "movies";
            
        } catch (Exception e) {
            logger.error("Batten down the hatches! Error during movie search: {}", e.getMessage(), e);
            model.addAttribute("movies", movieService.getAllMovies());
            model.addAttribute("genres", movieService.getAllGenres());
            model.addAttribute("searchMessage", 
                "Arrr! Something went wrong with the search, me hearty. Try again!");
            model.addAttribute("searchPerformed", true);
            return "movies";
        }
    }

    /**
     * Arrr! This be the JSON API endpoint for search - perfect for other ships (applications)
     * that want to communicate with our movie service!
     * 
     * @param name Movie name to search for (optional)
     * @param id Movie ID to search for (optional)
     * @param genre Genre to filter by (optional)
     * @return JSON response with search results and metadata
     */
    @GetMapping("/movies/search/api")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchMoviesApi(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "genre", required = false) String genre) {
        
        logger.info("Ahoy! API search requested with name: {}, id: {}, genre: {}", name, id, genre);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate search parameters
            if ((name == null || name.trim().isEmpty()) && 
                id == null && 
                (genre == null || genre.trim().isEmpty())) {
                
                response.put("success", false);
                response.put("message", "Arrr! At least one search parameter be required, matey!");
                response.put("movies", List.of());
                response.put("totalResults", 0);
                return ResponseEntity.badRequest().body(response);
            }
            
            // Perform the search
            List<Movie> searchResults = movieService.searchMovies(name, id, genre);
            
            // Build successful response
            response.put("success", true);
            response.put("movies", searchResults);
            response.put("totalResults", searchResults.size());
            
            if (searchResults.isEmpty()) {
                response.put("message", "No movies found matching yer search criteria, ye scurvy dog!");
            } else {
                response.put("message", String.format("Found %d movies in our treasure chest!", searchResults.size()));
            }
            
            // Add search parameters to response for reference
            Map<String, Object> searchParams = new HashMap<>();
            searchParams.put("name", name);
            searchParams.put("id", id);
            searchParams.put("genre", genre);
            response.put("searchParameters", searchParams);
            
            logger.info("API search successful, found {} movies", searchResults.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Batten down the hatches! Error during API search: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Arrr! Something went wrong with the search. Try again later, me hearty!");
            response.put("movies", List.of());
            response.put("totalResults", 0);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/movies/{id}/details")
    public String getMovieDetails(@PathVariable("id") Long movieId, org.springframework.ui.Model model) {
        logger.info("Fetching details for movie ID: {}", movieId);
        
        Optional<Movie> movieOpt = movieService.getMovieById(movieId);
        if (!movieOpt.isPresent()) {
            logger.warn("Movie with ID {} not found", movieId);
            model.addAttribute("title", "Movie Not Found");
            model.addAttribute("message", "Movie with ID " + movieId + " was not found.");
            return "error";
        }
        
        Movie movie = movieOpt.get();
        model.addAttribute("movie", movie);
        model.addAttribute("movieIcon", MovieIconUtils.getMovieIcon(movie.getMovieName()));
        model.addAttribute("allReviews", reviewService.getReviewsForMovie(movie.getId()));
        
        return "movie-details";
    }
}